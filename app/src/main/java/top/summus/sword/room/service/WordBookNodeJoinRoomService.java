package top.summus.sword.room.service;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import lombok.AllArgsConstructor;
import top.summus.sword.room.dao.WordBookNodeJoinDao;
import top.summus.sword.room.entity.BookNode;
import top.summus.sword.room.entity.Word;
import top.summus.sword.room.entity.WordBookNodeJoin;

@AllArgsConstructor
public class WordBookNodeJoinRoomService {
    WordBookNodeJoinDao wordBookNodeJoinDao;

    public Single<Long> insert(WordBookNodeJoin entity) {
        return wordBookNodeJoinDao.insert(entity);
    }

    public Single<Long> insert(long wordId, long bookNodeId) {
        WordBookNodeJoin wordBookNodeJoin = WordBookNodeJoin.builder().wordId(wordId).bookNodeId(bookNodeId).build();
        return wordBookNodeJoinDao.insert(wordBookNodeJoin);
    }

    public Completable update(WordBookNodeJoin entity) {
        return wordBookNodeJoinDao.update(entity);
    }

    public Completable delete(WordBookNodeJoin entity) {
        return wordBookNodeJoinDao.delete(entity);
    }

    public Single<WordBookNodeJoin> selectByPrimary(long id) {
        return wordBookNodeJoinDao.selectByPrimary(id);
    }

    public Single<List<WordBookNodeJoin>> selectByWordIdAndBookNodeId(long wordId, long bookNodeId) {
        return wordBookNodeJoinDao.selectByWordIdAndBookNodeId(wordId, bookNodeId);
    }

    public Single<List<Word>> selectWordsByBookNodeId(long bookNodeId) {
        return wordBookNodeJoinDao.selectWordsByBookNodeId(bookNodeId);
    }

    public Single<List<BookNode>> selectBookNodesByWordId(long wordId) {
        return wordBookNodeJoinDao.selectBookNodesByWordId(wordId);
    }


}
