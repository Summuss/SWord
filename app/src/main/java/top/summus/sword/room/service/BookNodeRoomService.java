package top.summus.sword.room.service;

import android.annotation.SuppressLint;
import android.util.Log;


import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;
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


    public Single<Long> insert(BookNode bookNode) {
        return bookNodeRoomDao.insert(bookNode).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public long insertSync(BookNode bookNode){
        return bookNodeRoomDao.insertSync(bookNode);
    }

    public Completable delete(BookNode bookNode) {
        return bookNodeRoomDao.delete(bookNode).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                ;
    }

    public Single<List<BookNode>> selectByPath(String path) {

        return bookNodeRoomDao.selectByPath(path)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    public List<BookNode> selectByNo(long no) {
        return bookNodeRoomDao.selectByNoSync(no);
    }

    public void update(BookNode bookNode) {
        bookNodeRoomDao.updateSync(bookNode);
    }

    public List<BookNode> selectToBePatched() {
        return bookNodeRoomDao.selectToBePatchedSynced();
    }

    public List<BookNode> selectToBePosted() {
        return bookNodeRoomDao.selectToBePostedSynced();
    }

    public BookNode selectByPrimary(long id) {
        return bookNodeRoomDao.selectByPrimary(id);
    }

    public void updateNodeNoByPrimary(long primary, long no) {
        bookNodeRoomDao.setNodeNoByPrimary(primary, no);
    }


}
