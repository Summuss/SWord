package top.summus.sword.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import top.summus.sword.room.dao.BookNodeDao;
import top.summus.sword.room.dao.CurrentStudyWordDao;
import top.summus.sword.room.dao.DeleteRecordDao;
import top.summus.sword.room.dao.MeaningDao;
import top.summus.sword.room.dao.SentenceDao;
import top.summus.sword.room.dao.WordBookNodeJoinDao;
import top.summus.sword.room.dao.WordDao;
import top.summus.sword.room.entity.BookNode;
import top.summus.sword.room.entity.CurrentStudyWord;
import top.summus.sword.room.entity.DeleteRecord;
import top.summus.sword.room.entity.Meaning;
import top.summus.sword.room.entity.Sentence;
import top.summus.sword.room.entity.Word;
import top.summus.sword.room.entity.WordBookNodeJoin;
import top.summus.sword.util.RoomConventer;

@Database(entities = {BookNode.class, DeleteRecord.class, Word.class, WordBookNodeJoin.class, Meaning.class, Sentence.class, CurrentStudyWord.class}, version = 12, exportSchema = false)
@TypeConverters({RoomConventer.class})
public abstract class SWordDatabase extends RoomDatabase {

    public abstract BookNodeDao getBookNodeDao();

    public abstract DeleteRecordDao getDeleteRecordDao();

    public abstract WordDao getWordDao();

    public abstract WordBookNodeJoinDao getWordBookNodeRelationDao();

    public abstract MeaningDao getMeaningDao();

    public abstract SentenceDao getSentenceDao();

    public abstract CurrentStudyWordDao getCurrentStudyWordDao();

}
