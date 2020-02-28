package top.summus.sword.viewmodel;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import lombok.Getter;
import okhttp3.OkHttpClient;
import top.summus.sword.SWordApplication;
import top.summus.sword.SWordDatabase;
import top.summus.sword.SWordSharedPreferences;
import top.summus.sword.api.BookNodeApi;
import top.summus.sword.api.TimeApi;
import top.summus.sword.dao.BookNodeDao;
import top.summus.sword.entity.BookNode;
import top.summus.sword.util.DateFormatUtil;

@SuppressLint("CheckResult")

public class BookNodeViewModel extends ViewModel {


    private static final String TAG = "BookNodeViewModel";

    @Inject
    BookNodeDao bookNodeDao;

    @Inject
    BookNodeApi bookNodeApi;

    @Inject
    TimeApi timeApi;

    @Inject
    SWordSharedPreferences sharedPreferences;

    private DataChangedListener callback;

    public static BookNodeViewModel getInstance(AppCompatActivity activity, DataChangedListener callback) {
        BookNodeViewModel bookNodeViewModel = new ViewModelProvider(activity).get(BookNodeViewModel.class);
        bookNodeViewModel.dependencyInject();
        bookNodeViewModel.callback = callback;
        bookNodeViewModel.loadData(activity);
        return bookNodeViewModel;
    }

    @Getter
    private MutableLiveData<String> currentPath = new MutableLiveData<>();

    @Getter
    private List<BookNode> bookNodesShowed = new ArrayList<>();

    private void dependencyInject() {
        SWordApplication.getAppComponent().inject(this);
        Objects.requireNonNull(bookNodeDao, "inject bookNodeDao failed");
        Log.i(TAG, "dependencyInject: " + sharedPreferences.hashCode());
    }


    /**
     * set the observer of {@link #currentPath}. when it is changed, get booNodes from database
     */
    public void loadData(AppCompatActivity activity) {
        currentPath.observe(activity, s -> {
            Log.i(TAG, "[loadData]  switch path to:" + s);
            bookNodeDao.selectByPath(s).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe((bookNodes, throwable) -> {
                        if (bookNodes != null) {
                            Log.i(TAG, "[loadData] get bookNodes from " + s + " successfully");
                            bookNodesShowed.clear();
                            bookNodesShowed.addAll(bookNodes);
                            callback.onPathSwished(s);
                        }
                        if (throwable != null) {
                            Log.e(TAG, "[loadData] get bookNodes from " + s + " fail", throwable);
                        }
                    });
        });
        currentPath.setValue("/");
        timeCorrect();
    }

    /**
     * insert bookNode into local database and callback {@link #insert(BookNode)} to refresh change
     *
     * <p>
     * <ol>
     * <li> get primary key from {@link BookNodeDao#insert(BookNode...)}.</li>
     * <li> get list of bookNode whose path equal inserted items'.</li>
     * <li> find the position which inserted item at  from list.</li>
     * <li> return a @{List} which contains primary key and position of inserted item.</li>
     * </ol>
     * </p>
     */
    public void insert(BookNode bookNode) {
        bookNodeDao.insert(bookNode).subscribeOn(Schedulers.io())
                .map(longs -> {

                    long key = longs.get(0);
                    List<BookNode> bookNodes = bookNodeDao.selectByPathBySync(currentPath.getValue());
                    long postion = -1;
                    for (int i = 0; i < bookNodes.size(); i++) {
                        if (key == bookNodes.get(i).getId()) {
                            postion = i;
                        }
                    }
                    List<Long> result = new ArrayList<>();
                    result.add(key);
                    result.add(postion);
                    return result;
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe((longs, throwable) -> {

                    if (longs != null) {
                        Log.i(TAG, "[insert]   insert successfully  " + bookNode);
                        long key = longs.get(0);

                        int position = longs.get(1).intValue();
                        Log.i(TAG, "[insert]  key= " + key);
                        Log.i(TAG, "[insert]  position= " + position);

                        bookNode.setId(key);
                        bookNodesShowed.add(position, bookNode);
                        callback.onInsertFinished(position);
                    }
                    if (throwable != null) {
                        Log.e(TAG, "[insert] insert failed  " + bookNode, throwable);
                    }
                });


    }


    public void delete(BookNode bookNode, int position) {
        bookNodeDao.delete(bookNode).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> {
                            Log.i(TAG, "[delete] delete successfully " + bookNode);
                            bookNodesShowed.remove(position);
                            callback.onDeleteFinished(position);

                        },
                        throwable -> Log.e(TAG, "[delete]  delete failed  " + bookNode, throwable)
                )
        ;

    }


    public void timeCorrect() {
        final OkHttpClient client = new OkHttpClient.Builder().
                readTimeout(100, TimeUnit.MILLISECONDS)
                .build();

        final long mills = System.currentTimeMillis();

        timeApi.getTime().subscribeOn(Schedulers.io())
                .subscribe(
                        voidResponse -> {
                            long currentMills = System.currentTimeMillis();
                            long delay = currentMills - mills;
                            Log.i(TAG, "[timeCorrect]  " + "delay " + delay);
                            if (voidResponse.isSuccessful()) {
                                Log.i(TAG, "[timeCorrect]  " + "get right response statusCode");
                                Log.i(TAG, "[timeCorrect]  " + "get server time: " + DateFormatUtil.parseDateToString(voidResponse.headers().getDate("Date")));
                                ;
                                long serverMills = voidResponse.headers().getDate("Date").getTime();
                                long gap = serverMills - delay - currentMills;

                                Log.i(TAG, "[timeCorrect]  " + "last gap is" + sharedPreferences.getTimeGap());
                                Log.i(TAG, "[timeCorrect]  " + "calculated gap is " + gap);
                                Log.i(TAG, "[timeCorrect]  " + "minus last gap is " + (gap - sharedPreferences.getTimeGap()));

                                sharedPreferences.setTimeGap(gap);
                                Log.i(TAG, "[timeCorrect]  " + "write gap to local file ");
                            } else {
                                Log.i(TAG, "[timeCorrect]  " + " get wrong response statusCode");
                            }
                        },
                        throwable -> Log.e(TAG, "[timeCorrect]  error!!", throwable)
                );
    }

    public interface DataChangedListener {
        /**
         * callback when {@link #loadData(AppCompatActivity)} is changed
         */
        void onPathSwished(String destinationPath);

        /**
         * callback when {@link #insert(BookNode)} is called
         *
         * @param position Position of element which should be refreshed for insert
         */
        void onInsertFinished(int position);

        /**
         * callback when {@link #delete(BookNode, int)} is called
         *
         * @param position Position of element which removed in viewModel
         */
        void onDeleteFinished(int position);

    }


}
