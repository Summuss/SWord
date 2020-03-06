package top.summus.sword.room.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import top.summus.sword.room.entity.Word;

@Dao
public interface WordDao extends BaseDao<Word> {

    @Query("SELECT * FROM word WHERE id=:id")
    Single<Word> selectByPrimary(long id);

    @Query("SELECT * FROM word")
    Single<List<Word>> selectAll();

    @Query("SELECT * FROM word WHERE content=:content")
    Single<List<Word>> selectByContent(String content);

}
