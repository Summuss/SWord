package top.summus.sword.room.service;

import android.annotation.SuppressLint;
import android.util.Log;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import lombok.AllArgsConstructor;
import top.summus.sword.room.dao.WordDao;
import top.summus.sword.room.entity.Word;

@SuppressLint("CheckResult")

@AllArgsConstructor
public class WordRoomService {
    private static final String TAG = "WordRoomService";
    WordDao wordDao;

    WordBookNodeJoinRoomService wordBookNodeJoinRoomService;

    public Single<Long> insert(Word entity) {
        return wordDao.insert(entity)
                .doOnSuccess(aLong -> {
                    Log.i(TAG, "insert: successfully " + entity);
                })
                .doOnError(throwable -> {
                    Log.e(TAG, "insert: " + entity, throwable);
                });

    }

    public Completable update(Word entity) {
        return wordDao.update(entity)
                .doOnComplete(() -> {
                    Log.i(TAG, "update: successfully " + entity);
                })
                .doOnError(throwable -> {
                    Log.e(TAG, "update: " + entity, throwable);
                });
    }

    public Completable delete(Word entity) {
        return wordDao.delete(entity)
                .doOnComplete(() -> {
                    Log.i(TAG, "delete: successfully " + entity);
                })
                .doOnError(throwable -> {
                    Log.e(TAG, "delete: " + entity, throwable);
                });
    }

    public Single<Word> selectByPrimary(long id) {
        return wordDao.selectByPrimary(id);
    }

    public Single<List<Word>> selectByContent(String content) {
        return wordDao.selectByContent(content);
    }

    public Single<List<Word>> selectAll() {
        return wordDao.selectAll();
    }

    public Single<List<Word>> add(Word word, long bookNodeId) {
        return selectByContent(word.getContent())
                .doOnSuccess(words -> {
                    //if this word  exist in db, find out whether it exist in the book
                    if (words.size() != 0) {
                        Log.i(TAG, "getMeaningNodes: word " + word + " already exist");
                        word.setId(words.get(0).getId());
                        wordBookNodeJoinRoomService.selectByWordIdAndBookNodeId(words.get(0).getId(), bookNodeId)
                                .subscribe((wordBookNodeJoins, throwable1) -> {
                                    //this word don't exist in the book, insert it into
                                    if (wordBookNodeJoins.size() == 0) {
                                        Log.i(TAG, "getMeaningNodes: wordBookNodeJoin not exist,insert");
                                        wordBookNodeJoinRoomService.insert(words.get(0).getId(), bookNodeId).subscribe();
                                    }
                                });
                    } else {
                        //this word do not exist in db, insert it into word table and create a wordBookNodeJoin
                        Log.i(TAG, "getMeaningNodes: word don't exist, insert");
                        insert(word)
                                .doFinally(() -> {
                                    wordBookNodeJoinRoomService.insert(word.getId(), bookNodeId).doOnSuccess(aLong -> {
                                        Log.i(TAG, "getMeaningNodes: insert wordBookNodeJoin successfully,get id" + aLong);
                                    }).subscribe();
                                })
                                .subscribe((aLong, throwable1) -> {
                                    if (aLong != null) {
                                        word.setId(aLong);
                                        Log.i(TAG, "getMeaningNodes: insert successfully, get id " + aLong);
                                    }
                                });
                    }
                })
                .doOnError(throwable -> {
                    Log.e(TAG, "getMeaningNodes: ", throwable);
                });

    }
}
