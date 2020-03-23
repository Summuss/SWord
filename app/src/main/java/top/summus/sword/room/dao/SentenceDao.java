package top.summus.sword.room.dao;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Single;
import top.summus.sword.room.entity.Sentence;

@Dao
public interface SentenceDao extends BaseDao<Sentence> {

    @Query("SELECT * FROM sentence WHERE id=:key")
    Single<List<Sentence>> selectByPrimary(long key);

    @Query("SELECT * FROM sentence WHERE meaning_id=:meaningId")
    Single<List<Sentence>> selectByMeaningId(long meaningId);
}
