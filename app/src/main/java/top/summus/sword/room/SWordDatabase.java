package top.summus.sword.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import top.summus.sword.room.dao.BookNodeRoomDao;
import top.summus.sword.room.dao.DeleteRecordDao;
import top.summus.sword.room.entity.BookNode;
import top.summus.sword.room.entity.DeleteRecord;
import top.summus.sword.util.RoomConventer;

@Database(entities = {BookNode.class, DeleteRecord.class}, version = 4, exportSchema = false)
@TypeConverters({RoomConventer.class})
public abstract class SWordDatabase extends RoomDatabase {

    public abstract BookNodeRoomDao getBookNodeDao();

    public abstract DeleteRecordDao getDeleteRecordDao();
}
