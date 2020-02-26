package top.summus.sword;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import top.summus.sword.dao.BookNodeDao;
import top.summus.sword.entity.BookNode;
import top.summus.sword.util.RoomConventer;

import static top.summus.sword.SWordApplication.getContext;

@Database(entities = {BookNode.class}, version = 3, exportSchema = false)
@TypeConverters({RoomConventer.class})
public abstract class SWordDatabase extends RoomDatabase {
    private static SWordDatabase database;

    public abstract BookNodeDao getBookNodeDao();

    public static synchronized SWordDatabase getInstance() {
        if (database == null) {
            database = Room.databaseBuilder(getContext(), SWordDatabase.class, "sword")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return database;
    }


}
