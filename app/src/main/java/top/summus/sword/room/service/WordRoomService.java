package top.summus.sword.room.service;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import lombok.AllArgsConstructor;
import top.summus.sword.room.dao.WordDao;
import top.summus.sword.room.entity.Word;

@AllArgsConstructor
public class WordRoomService {
    WordDao wordDao;

    public Single<Long> insert(Word entity) {
        return wordDao.insert(entity);
    }

    public Completable update(Word entity) {
        return wordDao.update(entity);
    }

    public Completable delete(Word entity) {
        return wordDao.delete(entity);
    }

    public Single<Word> selectByPrimary(long id) {
        return wordDao.selectByPrimary(id);
    }

    public Single<List<Word>> selectAll() {
        return wordDao.selectAll();
    }
}
