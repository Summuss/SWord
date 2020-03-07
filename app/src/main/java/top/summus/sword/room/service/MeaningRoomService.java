package top.summus.sword.room.service;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import lombok.AllArgsConstructor;
import top.summus.sword.room.dao.MeaningDao;
import top.summus.sword.room.entity.Meaning;

@AllArgsConstructor
public class MeaningRoomService {
    MeaningDao meaningDao;

    public Single<Long> insert(Meaning entity) {
        return meaningDao.insert(entity);
    }

    public Completable update(Meaning entity) {
        return meaningDao.update(entity);
    }

    public Completable delete(Meaning entity) {
        return meaningDao.delete(entity);
    }


    public Single<List<Meaning>> selectByPrimary(long key) {
        return meaningDao.selectByPrimary(key);
    }

}
