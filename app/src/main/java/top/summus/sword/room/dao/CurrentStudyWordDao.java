package top.summus.sword.room.dao;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Single;
import top.summus.sword.room.entity.CurrentStudyWord;
import top.summus.sword.room.entity.Word;

@Dao
public interface CurrentStudyWordDao extends BaseDao<CurrentStudyWord> {
    @Query("SELECT * FROM current_study_word WHERE id=:id")
    Single<CurrentStudyWord> selectByPrimary(long id);

    @Query("SELECT word.* FROM word,current_study_word WHERE word.id=current_study_word.word_id")
    Single<List<Word>> selectToBeLeaned();

    @Query("SELECT * FROM current_study_word")
    Single<List<CurrentStudyWord>> selectAll();
}
