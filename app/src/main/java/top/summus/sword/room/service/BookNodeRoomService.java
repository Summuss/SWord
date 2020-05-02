package top.summus.sword.room.service;

import android.annotation.SuppressLint;
import android.database.sqlite.SQLiteConstraintException;
import android.util.Log;


import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import lombok.AllArgsConstructor;
import top.summus.sword.room.entity.BookNode;
import top.summus.sword.room.dao.BookNodeDao;

import static top.summus.sword.room.dao.DeleteRecordDao.Table.BOOK_NODE;

@SuppressLint("CheckResult")
@AllArgsConstructor
public class BookNodeRoomService {
    private static final String TAG = "BookNodeRoomService";


    BookNodeDao bookNodeDao;

    DeleteRecordRoomService deleteRecordRoomService;


    public Single<Long> insert(BookNode bookNode) {
        return bookNodeDao.insert(bookNode);
    }

    public long insertSync(BookNode bookNode) {
        return bookNodeDao.insertSync(bookNode);
    }

    public Observable<BookNode> deleteIntoDeleteRecord(BookNode bookNode) {
        if (bookNode.getNodeTag() == 0) {
            return selectChildNodes(bookNode).subscribeOn(Schedulers.io())
                    .toObservable()
                    .flatMap((Function<List<BookNode>, ObservableSource<BookNode>>) Observable::fromIterable)
                    .doOnNext(bookNode1 -> {
                        Log.i(TAG, "deleteIntoDeleteRecord: " + bookNode1.getNodeName() + "  " + bookNode1.getNodeNo());
                        deleteRecordRoomService.insert(BOOK_NODE, bookNode1.getNodeNo())
                                .subscribe((aLong, throwable) -> {
                                    if (throwable != null) {
                                        Log.e(TAG, "deleteIntoDeleteRecord: ", throwable);
                                    }
                                });
                        bookNodeDao.delete(bookNode1).subscribe();
                    })
                    .doOnError(throwable -> {
                        Log.e(TAG, "deleteIntoDeleteRecord: ", throwable);
                    })
                    .doOnComplete(() -> {
                        deleteRecordRoomService.insert(BOOK_NODE, bookNode.getNodeNo())
                                .subscribe((aLong, throwable) -> {
                                    if (throwable != null) {
                                        Log.e(TAG, "deleteIntoDeleteRecord: ", throwable);
                                    }
                                });
                        bookNodeDao.delete(bookNode).subscribe();
                    });
        } else {

            Log.i(TAG, "deleteIntoDeleteRecord: d");
            return deleteRecordRoomService.insert(BOOK_NODE, bookNode.getNodeNo())
                    .toObservable()
                    .subscribeOn(Schedulers.io())
                    .map(aLong -> bookNode)
                    .doOnComplete(() -> {
                        bookNodeDao.delete(bookNode).subscribe();
                    })
                    .doOnError(throwable -> {
                        Log.e(TAG, "deleteIntoDeleteRecord: ", throwable);
                        if (throwable instanceof SQLiteConstraintException) {
                            bookNodeDao.delete(bookNode).subscribe();
                        }
                    });
        }

    }

    public Observable<BookNode> delete(BookNode bookNode) {
        if (bookNode.getNodeTag() == 0) {
            return selectChildNodes(bookNode)
                    .toObservable()
                    .flatMap((Function<List<BookNode>, ObservableSource<BookNode>>) Observable::fromIterable)
                    .doOnNext(bookNode1 -> {
                        bookNodeDao.delete(bookNode1).subscribe();
                    })
                    .doOnError(throwable -> {
                        Log.e(TAG, "deleteIntoDeleteRecord: ", throwable);
                    })
                    .doOnComplete(() -> {
                        bookNodeDao.delete(bookNode).subscribe();
                    });
        } else {
            return bookNodeDao.delete(bookNode).toObservable();
        }

    }


    public Single<List<BookNode>> selectByPath(String path) {

        return bookNodeDao.selectByPath(path)
                ;
    }

    public List<Long> selectAllNodeNoSync() {
        return bookNodeDao.selectAllNodeNoSync();
    }

    public void deleteByNodeNo(long nodeNo) {
        bookNodeDao.deleteByNodeNo(nodeNo);
    }

    public List<BookNode> selectByNoSync(long no) {
        return bookNodeDao.selectByNoSync(no);
    }

    public void updateSync(BookNode bookNode) {
        bookNodeDao.updateSync(bookNode);
    }

    public List<BookNode> selectToBePatchedSync() {
        return bookNodeDao.selectToBePatchedSynced();
    }

    public List<BookNode> selectToBePostedSync() {
        return bookNodeDao.selectToBePostedSynced();
    }

    public BookNode selectByPrimarySync(long id) {
        return bookNodeDao.selectByPrimary(id);
    }

    public void updateNodeNoByPrimarySync(long primary, long no) {
        bookNodeDao.setNodeNoByPrimary(primary, no);
    }

    public Single<List<BookNode>> selectChildNodes(BookNode bookNode) {
        String path = bookNode.getNodePath() + bookNode.getNodeName() + "%";
        return bookNodeDao.selectPathLike(path)
                ;
    }

}
