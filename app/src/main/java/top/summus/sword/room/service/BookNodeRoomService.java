package top.summus.sword.room.service;

import android.annotation.SuppressLint;
import android.util.Log;


import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import lombok.NoArgsConstructor;
import top.summus.sword.SWordApplication;
import top.summus.sword.entity.BookNode;
import top.summus.sword.exception.MethodNotImplementedException;
import top.summus.sword.room.dao.BookNodeRoomDao;

@SuppressLint("CheckResult")
public class BookNodeRoomService {
    private static final String TAG = "BookNodeRoomService";


    BookNodeRoomDao bookNodeRoomDao;

    public BookNodeRoomService(BookNodeRoomDao bookNodeRoomDao) {
        this.bookNodeRoomDao = bookNodeRoomDao;
    }


    public void insert(BookNode bookNode, InsertCallback callback) {
        bookNodeRoomDao.insert(bookNode).subscribeOn(Schedulers.io())
                .subscribe((longs, throwable) -> {
                    if (longs != null) {
                        bookNode.setId(longs.get(0));
                        Log.i(TAG, "insert: insert successfully  " + bookNode);
                        callback.onInsertFinishedSuccess(bookNode);
                    }
                    if (throwable != null) {
                        Log.e(TAG, "insert: insert failed  " + bookNode, throwable);
                        callback.onInsertFinishedError(bookNode, throwable);
                    }
                });
    }

    public void delete(BookNode bookNode, DeleteCallback callback) {
        bookNodeRoomDao.delete(bookNode).subscribeOn(Schedulers.io())
                .subscribe(
                        () -> {
                            Log.i(TAG, "delete: delete successfully  " + bookNode);
                            callback.onDeleteFinished(bookNode);
                        },
                        throwable -> Log.e(TAG, "delete", throwable)
                );
    }

    public void selectByPath(String path, SelectByPathCallback callback) {
        bookNodeRoomDao.selectByPath(path).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((bookNodes, throwable) -> {
                    if (bookNodes != null) {
                        Log.i(TAG, "selectByPath: success");
                        if (callback != null) {
                            callback.onSelectByPathFinished(bookNodes);
                        }
                    }
                    if (throwable != null) {
                        Log.e(TAG, "selectByPath", throwable);
                    }
                });
    }


    public interface InsertCallback {
        void onInsertFinishedSuccess(BookNode bookNode);

        void onInsertFinishedError(BookNode bookNode, Throwable throwable);
    }

    public interface DeleteCallback {

        void onDeleteFinished(BookNode bookNode);
    }

    public interface SelectByPathCallback {

        void onSelectByPathFinished(List<BookNode> bookNodes);
    }
}
