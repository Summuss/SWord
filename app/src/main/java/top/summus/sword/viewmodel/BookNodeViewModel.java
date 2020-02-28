package top.summus.sword.viewmodel;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import lombok.Getter;
import top.summus.sword.SWordApplication;
import top.summus.sword.network.api.BookNodeApi;
import top.summus.sword.network.service.BookNodeHttpService;
import top.summus.sword.room.dao.BookNodeRoomDao;
import top.summus.sword.entity.BookNode;
import top.summus.sword.network.service.TimeHttpService;
import top.summus.sword.room.service.BookNodeRoomService;

@SuppressLint("CheckResult")

public class BookNodeViewModel extends ViewModel {


    private static final String TAG = "BookNodeViewModel";

    @Inject
    BookNodeRoomService bookNodeRoomService;

    @Inject
    BookNodeHttpService bookNodeHttpService;

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
    }


    /**
     * set the observer of {@link #currentPath}. when it is changed, get booNodes from database
     */
    private void loadData(AppCompatActivity activity) {
        currentPath.observe(activity, s -> {
            bookNodeRoomService.selectByPath(s, bookNodes -> {
                bookNodesShowed.clear();
                bookNodesShowed.addAll(bookNodes);
                callback.onPathSwished(s);
            });
        });
        currentPath.setValue("/");
        bookNodeHttpService.downloadBookNodes(new BookNodeHttpService.DownloadFinishedSuccessCallback() {
            @Override
            public void onDownloadSucceeded() {
                Log.i(TAG, "onDownloadSucceeded: ");
            }

            @Override
            public void onDownloadErrored(Throwable throwable) {
                Log.e(TAG, "onDownloadErrored: ", throwable);

            }
        });


    }

    /**
     * insert bookNode into local database and callback {@link #insert(BookNode)} to refresh change
     *
     * <p>
     * <ol>
     * <li> get primary key from {@link BookNodeRoomDao#insert(BookNode...)}.</li>
     * <li> get list of bookNode whose path equal inserted items'.</li>
     * <li> find the position which inserted item at  from list.</li>
     * <li> return a @{List} which contains primary key and position of inserted item.</li>
     * </ol>
     * </p>
     */

    private int findPositionInBookNodes(BookNode target, List<BookNode> bookNodeList) {

        for (int i = 0; i < bookNodeList.size(); i++) {
            BookNode bookNode = bookNodeList.get(i);
            boolean condition1 = bookNode.getNodeTag() > target.getNodeTag();
            boolean condition2 = bookNode.getNodeName().compareTo(target.getNodeName()) > 0;
            if (condition1 || condition2) {
                return i;
            }
        }
        return bookNodeList.size();
    }

    public void insert(BookNode bookNode) {
        bookNodeRoomService.insert(bookNode, new BookNodeRoomService.InsertCallback() {
            @Override
            public void onInsertFinishedSuccess(BookNode bookNode) {
                Log.i(TAG, "onInsertFinishedSuccess: " + bookNode);
                int positionInBookNodes = findPositionInBookNodes(bookNode, bookNodesShowed);
                bookNodesShowed.add(positionInBookNodes, bookNode);
                callback.onInsertFinished(positionInBookNodes);
            }

            @Override
            public void onInsertFinishedError(BookNode bookNode, Throwable throwable) {
                Log.e(TAG, "onInsertFinishedError: ", throwable);
            }
        });
    }


    public void delete(BookNode target, int position) {
        bookNodeRoomService.delete(target, bookNode -> {
            Log.i(TAG, "delete: successfully  " + target);
            callback.onDeleteFinished(position);
        });
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
