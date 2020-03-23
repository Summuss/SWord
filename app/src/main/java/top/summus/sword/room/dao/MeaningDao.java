package top.summus.sword.room.dao;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Single;
import top.summus.sword.room.entity.Meaning;

@Dao
public interface MeaningDao extends BaseDao<Meaning> {

    @Query("SELECT * FROM meaning WHERE id=:key")
    Single<List<Meaning>> selectByPrimary(long key);

    @Query("SELECT * FROM meaning WHERE word_id=:wordId")
    Single<List<Meaning>> selectByWordId(long wordId);


}
