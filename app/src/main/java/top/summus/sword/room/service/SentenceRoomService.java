package top.summus.sword.room.service;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import lombok.AllArgsConstructor;
import top.summus.sword.room.dao.SentenceDao;
import top.summus.sword.room.entity.Sentence;

@AllArgsConstructor
public class SentenceRoomService {
    SentenceDao sentenceDao;

    public Single<Long> insert(Sentence entity) {
        return sentenceDao.insert(entity);
    }

    public Completable update(Sentence entity) {
        return sentenceDao.update(entity);
    }

    public Completable delete(Sentence entity) {
        return sentenceDao.delete(entity);
    }

    public Single<List<Sentence>> selectByPrimary(long key) {
        return sentenceDao.selectByPrimary(key);
    }

}
