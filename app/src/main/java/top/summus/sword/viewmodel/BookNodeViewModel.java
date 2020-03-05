package top.summus.sword.viewmodel;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import javax.inject.Inject;

import es.dmoral.toasty.Toasty;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;
import lombok.Getter;
import lombok.Setter;
import top.summus.sword.SWordApplication;
import top.summus.sword.network.service.BookNodeHttpService;
import top.summus.sword.network.service.DeleteRecordHttpService;
import top.summus.sword.network.service.ErrorCollectionService;
import top.summus.sword.room.dao.BookNodeDao;
import top.summus.sword.room.entity.BookNode;
import top.summus.sword.network.service.TimeHttpService;
import top.summus.sword.room.service.BookNodeRoomService;
import top.summus.sword.room.service.DeleteRecordRoomService;

@SuppressLint("CheckResult")

public class BookNodeViewModel extends ViewModel {


    private static final String TAG = "BookNodeViewModel";

    @Inject
    BookNodeRoomService bookNodeRoomService;

    @Inject
    BookNodeHttpService bookNodeHttpService;

    @Inject
    TimeHttpService timeHttpService;

    @Inject
    DeleteRecordRoomService deleteRecordRoomService;

    @Inject
    DeleteRecordHttpService deleteRecordHttpService;

    @Inject
    ErrorCollectionService errorCollectionService;

    private DataChangedListener callback;

    @Getter
    @Setter
    private String currentPath = "/";

    @Getter
    private List<BookNode> bookNodesShowed = new ArrayList<>();

    public static BookNodeViewModel getInstance(AppCompatActivity activity, DataChangedListener callback) {
        BookNodeViewModel bookNodeViewModel = new ViewModelProvider(activity).get(BookNodeViewModel.class);
        bookNodeViewModel.dependencyInject();
        bookNodeViewModel.callback = callback;
        bookNodeViewModel.switchPath("/");
        return bookNodeViewModel;
    }


    public void sync(Action callback) throws Exception {

        Completable.create(emitter -> {
            Semaphore semaphore = new Semaphore(0, true);

            deleteRecordHttpService.syncDeleteRecord().subscribeOn(Schedulers.io())
                    .doFinally(() -> {
                        bookNodeHttpService.syncBookNodes()
                                .doFinally(semaphore::release)
                                .subscribe();
                    })
                    .subscribe();
            semaphore.acquire();
            emitter.onComplete();
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                   errorCollectionService.printAll();
                   if(errorCollectionService.hasConnectOut()){
                       Toasty.error(SWordApplication.getContext(),"connect out",Toasty.LENGTH_SHORT).show();
                   }
                   if(errorCollectionService.isNoError()){
                       Toasty.success(SWordApplication.getContext(),"sync success",Toasty.LENGTH_SHORT).show();
                   }
                   errorCollectionService.clear();
                    switchPath(currentPath);
                    callback.run();
                })
                .subscribe();


    }

    public void switchPath(String path) {
        Log.i(TAG, "switchPath: " + path);
        currentPath = path;
        bookNodeRoomService.selectByPath(path).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe((bookNodeList, throwable) -> {
                    if (bookNodeList != null) {
                        bookNodesShowed.clear();
                        bookNodesShowed.addAll(bookNodeList);
                        callback.onPathSwished(path);
                    }
                    if (throwable != null) {
                        Log.e(TAG, "switchPath", throwable);
                    }
                });
    }


    private void dependencyInject() {
        SWordApplication.getAppComponent().inject(this);
    }


    /**
     * insert bookNode into local database and callback {@link #insert(BookNode)} to refresh change
     *
     * <p>
     * <ol>
     * <li> get primary key from {@link BookNodeDao#insert(BookNode)}.</li>
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


    public void insert(BookNode target) {
        bookNodeRoomService.insert(target).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe((aLong, throwable) -> {
                    if (aLong != null) {
                        Log.i(TAG, "onInsertFinishedSuccess: " + target);
                        int positionInBookNodes = findPositionInBookNodes(target, bookNodesShowed);
                        bookNodesShowed.add(positionInBookNodes, target);
                        callback.onInsertFinished(positionInBookNodes);
                    }
                    if (throwable != null) {
                        Log.e(TAG, "onInsertFinishedError: " + target, throwable);

                    }
                });
    }

    public void delete(BookNode target, int position) {
        bookNodeRoomService.deleteIntoDeleteRecord(target)
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    bookNodesShowed.remove(position);
                    callback.onDeleteFinished(position);
                })
                .subscribe(bookNode -> {
                }, throwable -> Log.e(TAG, "deleteIntoDeleteRecord: ", throwable));
    }


    public interface DataChangedListener {
        /**
         *
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
