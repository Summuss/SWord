package top.summus.sword.room.service;

import android.util.Log;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import lombok.AllArgsConstructor;
import top.summus.sword.room.dao.SentenceDao;
import top.summus.sword.room.entity.Sentence;

@AllArgsConstructor
public class SentenceRoomService {
    private static final String TAG = "SentenceRoomService";
    SentenceDao sentenceDao;

    public Single<Long> insert(Sentence entity) {
        return sentenceDao.insert(entity)
                .doOnSuccess(aLong -> {
                    Log.i(TAG, "insert: successfully " + entity);
                })
                .doOnError(throwable -> {
                    Log.e(TAG, "insert: " + entity, throwable);
                });
    }

    public Completable update(Sentence entity) {
        return sentenceDao.update(entity)
                .doOnComplete(() -> {
                    Log.i(TAG, "update: successfully " + entity);
                })
                .doOnError(throwable -> {
                    Log.e(TAG, "update: " + entity, throwable);
                });
    }

    public Completable delete(Sentence entity) {
        return sentenceDao.delete(entity)
                .doOnComplete(() -> {
                    Log.i(TAG, "delete: successfully " + entity);
                })
                .doOnError(throwable -> {
                    Log.e(TAG, "delete: " + entity, throwable);
                });
    }

    public Single<List<Sentence>> selectByPrimary(long key) {
        return sentenceDao.selectByPrimary(key);
    }

    public Single<List<Sentence>> selectByMeaningId(long meaningId) {
        return sentenceDao.selectByMeaningId(meaningId)
                .doOnSuccess(sentences -> {
                    Log.i(TAG, "selectByMeaningId: successfully, get " + sentences);
                })
                .doOnError(throwable -> Log.e(TAG, "selectByMeaningId: ", throwable));
    }

}
