package top.summus.sword.room.service;

import android.util.Log;

import androidx.room.Query;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import lombok.AllArgsConstructor;
import top.summus.sword.room.dao.CurrentStudyWordDao;
import top.summus.sword.room.entity.CurrentStudyWord;
import top.summus.sword.room.entity.Word;

@AllArgsConstructor
public class CurrentStudyWordRoomService {
    private static final String TAG = "CurrentStudyRoomService";
    CurrentStudyWordDao currentStudyWordDao;

    public Single<Long> insert(CurrentStudyWord entity) {
        return currentStudyWordDao.insert(entity)
                .doOnSuccess(aLong -> {
                    Log.i(TAG, "insert: successfully " + entity);
                })
                .doOnError(throwable -> {
                    Log.e(TAG, "insert: " + entity, throwable);
                });

    }

    public Completable update(CurrentStudyWord entity) {
        return currentStudyWordDao.update(entity)
                .doOnComplete(() -> {
                    Log.i(TAG, "update: successfully " + entity);
                })
                .doOnError(throwable -> {
                    Log.e(TAG, "update: " + entity, throwable);
                });
    }

    public Completable delete(CurrentStudyWord entity) {
        return currentStudyWordDao.delete(entity)
                .doOnComplete(() -> {
                    Log.i(TAG, "delete: successfully " + entity);
                })
                .doOnError(throwable -> {
                    Log.e(TAG, "delete: " + entity, throwable);
                });
    }

    public Single<List<Word>> selectToBeLeaned() {
        return currentStudyWordDao.selectToBeLeaned()
                .doOnSuccess(words -> {
                    Log.i(TAG, "selectToBeLeaned: " + words.toString());
                })
                .doOnError(throwable -> {
                    Log.e(TAG, "selectToBeLeaned: ", throwable);
                });
    }


    public Single<List<CurrentStudyWord>> selectAll() {
        return currentStudyWordDao.selectAll();
    }

    public Completable deleteByWordId(long wordId) {
        return currentStudyWordDao.deleteByWordId(wordId);
    }

}
