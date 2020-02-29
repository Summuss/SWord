package top.summus.sword.room.service;

import android.annotation.SuppressLint;
import android.util.Log;


import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.functions.BiConsumer;
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


    public void insert(BookNode bookNode, BiConsumer<BookNode, Throwable> onCallback) {
        bookNodeRoomDao.insert(bookNode).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((longs, throwable) -> {
                    if (longs != null) {
                        bookNode.setId(longs.get(0));
                        Log.i(TAG, "insert: insert successfully  " + bookNode);
                    }
                    if (throwable != null) {
                        Log.e(TAG, "insert: insert failed  " + bookNode, throwable);
                    }
                    onCallback.accept(bookNode, throwable);
                });
    }

    public long insert(BookNode bookNode) {
        List<Long> longs = bookNodeRoomDao.insertSynced(bookNode);
        if (longs != null) {
            return longs.get(0);

        }
        return 0;
    }

    public void delete(BookNode bookNode, Action onDeleteComplete) {
        bookNodeRoomDao.delete(bookNode).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> {
                            Log.i(TAG, "delete: delete successfully  " + bookNode);
                            onDeleteComplete.run();
                        },
                        throwable -> Log.e(TAG, "delete", throwable)
                );
    }

    public void selectByPath(String path, BiConsumer<List<BookNode>, Throwable> callback) {
        bookNodeRoomDao.selectByPath(path).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((bookNodes, throwable) -> {
                    if (bookNodes != null) {
                        Log.i(TAG, "selectByPath: success");
                    }
                    if (throwable != null) {
                        Log.e(TAG, "selectByPath", throwable);
                    }
                    callback.accept(bookNodes, throwable);
                });
    }

    public void selectByNo(long no, BiConsumer<List<BookNode>, Throwable> callback) {
        bookNodeRoomDao.selectByNo(no).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((bookNodeList, throwable) -> {
                    if (bookNodeList != null) {
                        Log.i(TAG, "selectByNo: success  " + bookNodeList);
                    }
                    if (throwable != null) {
                        Log.e(TAG, "selectByNo: error!!", throwable);
                    }
                    callback.accept(bookNodeList, throwable);
                });
    }

    public List<BookNode> selectByNo(long no) {
        return bookNodeRoomDao.selectByNoSync(no);
    }

    public void update(BookNode bookNode) {
        bookNodeRoomDao.updateSync(bookNode);
    }

    public interface SelectByNoCallback {

        void selectByNoSucceeded(List<BookNode> bookNodeList);

        void selectByNoErrored(Throwable throwable);
    }



}
