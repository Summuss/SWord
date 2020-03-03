package top.summus.sword.room.service;

import android.annotation.SuppressLint;
import android.util.Log;


import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import lombok.AllArgsConstructor;
import top.summus.sword.room.entity.BookNode;
import top.summus.sword.room.dao.BookNodeRoomDao;

import static top.summus.sword.room.dao.DeleteRecordDao.Table.BOOK_NODE;

@SuppressLint("CheckResult")
@AllArgsConstructor
public class BookNodeRoomService {
    private static final String TAG = "BookNodeRoomService";


    BookNodeRoomDao bookNodeRoomDao;

    DeleteRecordRoomService deleteRecordRoomService;


    public Single<Long> insert(BookNode bookNode) {
        return bookNodeRoomDao.insert(bookNode);
    }

    public long insertSync(BookNode bookNode) {
        return bookNodeRoomDao.insertSync(bookNode);
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
                        bookNodeRoomDao.delete(bookNode1).subscribe();
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
                        bookNodeRoomDao.delete(bookNode).subscribe();
                    });
        } else {
            return deleteRecordRoomService.insert(BOOK_NODE, bookNode.getNodeNo())
                    .toObservable()
                    .subscribeOn(Schedulers.io())
                    .map(aLong -> bookNode)
                    .doOnComplete(() -> {
                        bookNodeRoomDao.delete(bookNode);
                    })
                    .doOnError(throwable -> Log.e(TAG, "deleteIntoDeleteRecord: ", throwable));
        }

    }

    public Observable<BookNode> delete(BookNode bookNode) {
        if (bookNode.getNodeTag() == 0) {
            return selectChildNodes(bookNode)
                    .toObservable()
                    .flatMap((Function<List<BookNode>, ObservableSource<BookNode>>) Observable::fromIterable)
                    .doOnNext(bookNode1 -> {
                        bookNodeRoomDao.delete(bookNode1).subscribe();
                    })
                    .doOnError(throwable -> {
                        Log.e(TAG, "deleteIntoDeleteRecord: ", throwable);
                    })
                    .doOnComplete(() -> {
                        bookNodeRoomDao.delete(bookNode).subscribe();
                    });
        } else {
            return bookNodeRoomDao.delete(bookNode).toObservable();
        }

    }


    public Single<List<BookNode>> selectByPath(String path) {

        return bookNodeRoomDao.selectByPath(path)
                ;
    }


    public List<BookNode> selectByNoSync(long no) {
        return bookNodeRoomDao.selectByNoSync(no);
    }

    public void updateSync(BookNode bookNode) {
        bookNodeRoomDao.updateSync(bookNode);
    }

    public List<BookNode> selectToBePatchedSync() {
        return bookNodeRoomDao.selectToBePatchedSynced();
    }

    public List<BookNode> selectToBePostedSync() {
        return bookNodeRoomDao.selectToBePostedSynced();
    }

    public BookNode selectByPrimarySync(long id) {
        return bookNodeRoomDao.selectByPrimary(id);
    }

    public void updateNodeNoByPrimarySync(long primary, long no) {
        bookNodeRoomDao.setNodeNoByPrimary(primary, no);
    }

    public Single<List<BookNode>> selectChildNodes(BookNode bookNode) {
        String path = bookNode.getNodePath() + bookNode.getNodeName() + "%";
        return bookNodeRoomDao.selectPathLike(path)
                ;
    }

}
