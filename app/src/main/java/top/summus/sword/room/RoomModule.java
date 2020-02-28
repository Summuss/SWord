package top.summus.sword.room;

import android.content.Context;

import androidx.room.Room;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import top.summus.sword.SWordDatabase;
import top.summus.sword.room.dao.BookNodeRoomDao;
import top.summus.sword.room.service.BookNodeRoomService;

@Module
public class RoomModule {
    @Singleton
    @Provides
    public SWordDatabase provideDatabase(Context context) {
        return Room.databaseBuilder(context, SWordDatabase.class, "sword")
                .fallbackToDestructiveMigration()
                .build();
    }

    @Singleton
    @Provides
    public BookNodeRoomDao provideBookNodeDao(SWordDatabase database) {
        return database.getBookNodeDao();
    }

    @Singleton
    @Provides
    public BookNodeRoomService provideBookNodeRoomService(BookNodeRoomDao bookNodeRoomDao) {
        return new BookNodeRoomService(bookNodeRoomDao);
    }
}
