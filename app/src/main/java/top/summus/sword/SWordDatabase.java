package top.summus.sword;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import top.summus.sword.dao.BookNodeDao;
import top.summus.sword.entity.BookNode;
import top.summus.sword.util.RoomConventer;

@Database(entities = {BookNode.class}, version = 3, exportSchema = false)
@TypeConverters({RoomConventer.class})
public abstract class SWordDatabase extends RoomDatabase {

    public abstract BookNodeDao getBookNodeDao();

}
