package top.summus.sword.room.service;

import android.util.Log;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import lombok.AllArgsConstructor;
import top.summus.sword.room.dao.MeaningDao;
import top.summus.sword.room.entity.Meaning;

@AllArgsConstructor
public class MeaningRoomService {
    private static final String TAG = "MeaningRoomService";
    MeaningDao meaningDao;

    public Single<Long> insert(Meaning entity) {
        return meaningDao.insert(entity)
                .doOnSuccess(aLong -> {
                    Log.i(TAG, "insert: successfully " + entity);
                })
                .doOnError(throwable -> {
                    Log.e(TAG, "insert: " + entity, throwable);
                });

    }

    public Completable update(Meaning entity) {
        return meaningDao.update(entity)
                .doOnComplete(() -> {
                    Log.i(TAG, "update: successfully " + entity);
                })
                .doOnError(throwable -> {
                    Log.e(TAG, "update: " + entity, throwable);
                });
    }

    public Completable delete(Meaning entity) {
        return meaningDao.delete(entity)
                .doOnComplete(() -> {
                    Log.i(TAG, "delete: successfully " + entity);
                })
                .doOnError(throwable -> {
                    Log.e(TAG, "delete: " + entity, throwable);
                });
    }


    public Single<List<Meaning>> selectByPrimary(long key) {
        return meaningDao.selectByPrimary(key);
    }

    public Single<List<Meaning>> selectByWordId(long wordId) {
        return meaningDao.selectByWordId(wordId)
                .doOnSuccess(meanings -> {
                    Log.i(TAG, "selectByWordId: successfully, get " + meanings);
                })
                .doOnError(throwable -> Log.e(TAG, "selectByWordId: ", throwable));
    }

}
