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

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import lombok.Getter;
import top.summus.sword.SWordDatabase;
import top.summus.sword.dao.BookNodeDao;
import top.summus.sword.entity.BookNode;

@SuppressLint("CheckResult")

public class BookNodeViewModel extends ViewModel {


    private static final String TAG = "BookNodeViewModel";
    private BookNodeDao bookNodeDao = SWordDatabase.getInstance().getBookNodeDao();
    private LifecycleOwner lifecycleOwner;
    private DataChangedListener callback;

    public static BookNodeViewModel getInstance(AppCompatActivity activity, DataChangedListener callback) {
        BookNodeViewModel bookNodeViewModel = new ViewModelProvider(activity).get(BookNodeViewModel.class);
        bookNodeViewModel.lifecycleOwner = activity;
        bookNodeViewModel.callback = callback;
        bookNodeViewModel.loadData(activity);
        return bookNodeViewModel;
    }

    @Getter
    private MutableLiveData<String> currentPath = new MutableLiveData<>();

    @Getter
    private List<BookNode> bookNodesShowed = new ArrayList<>();


    public void loadData(AppCompatActivity activity) {
        currentPath.observe(activity, s -> {
            Log.i(TAG, "BookNodeViewModel: switch path:" + s);
            bookNodeDao.selectByPath(s).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe((bookNodes, throwable) -> {
                        if (bookNodes != null) {
                            bookNodesShowed.clear();
                            bookNodesShowed.addAll(bookNodes);
                            callback.onPathSwished(s);
                        }
                        if (throwable != null) {
                            throwable.printStackTrace();
                        }
                    });
        });
        currentPath.setValue("/");
    }


    public void insert(BookNode bookNode) {
        bookNodeDao.insert(bookNode).subscribeOn(Schedulers.io())
                .map(new Function<List<Long>, List<Long>>() {
                    @Override
                    public List<Long> apply(List<Long> longs) throws Exception {

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
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe((longs, throwable) -> {

                    if (longs != null) {
                        Log.i(TAG, "insert: success");
                        long key = longs.get(0);

                        int position = new Long(longs.get(1)).intValue();

                        Log.i(TAG, bookNode.toString());
                        Log.i(TAG, "key=" + key);
                        Log.i(TAG, "position=" + position);
                        bookNode.setId(key);
                        bookNodesShowed.add(position, bookNode);
                        callback.onInsertFinished(position);
                    }
                    if (throwable != null) {
                        Log.i(TAG, "insert: error");
                        throwable.printStackTrace();
                    }
                });


    }


    public void delete(BookNode bookNode, int position) {
        bookNodeDao.delete(bookNode).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> {
                            Log.i(TAG, "delete success:" + bookNode);
                            bookNodesShowed.remove(position);
                            callback.onDeleteFinished(position);

                        },
                        Throwable::printStackTrace)
        ;

    }


    public void updateShowed() {
        bookNodesShowed = null;
        Log.i("BookNodeFragment", "updateShowed: " + Thread.currentThread().getName());
//        bookNodesShowed = bookNodeDao.selectByPath(currentPath.getValue());

    }

    public interface DataChangedListener {
        /**
         * callback when {@code currentPath} is changed
         */
        void onPathSwished(String destinationPath);

        /**
         * callback when {@code insert()} is called
         *
         * @param position Position of element which should be refreshed for insert
         */
        void onInsertFinished(int position);

        /**
         * callback when {@code delete()} is called
         *
         * @param position Position of element which removed in viewModel
         */
        void onDeleteFinished(int position);

    }


}
