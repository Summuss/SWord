package top.summus.sword.room;

import android.content.Context;

import androidx.room.Room;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import top.summus.sword.room.dao.BookNodeRoomDao;
import top.summus.sword.room.dao.DeleteRecordDao;
import top.summus.sword.room.service.BookNodeRoomService;
import top.summus.sword.room.service.DeleteRecordRoomService;

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
    public BookNodeRoomService provideBookNodeRoomService(BookNodeRoomDao bookNodeRoomDao,DeleteRecordRoomService recordRoomService) {
        return new BookNodeRoomService(bookNodeRoomDao,recordRoomService);
    }

    @Singleton
    @Provides
    public DeleteRecordDao provideDeleteReocrdDao(SWordDatabase sWordDatabase){
        return sWordDatabase.getDeleteRecordDao();
    }

    @Singleton
    @Provides
    public DeleteRecordRoomService provideDeleteRecordRoomService(DeleteRecordDao deleteRecordDao){
        return new DeleteRecordRoomService(deleteRecordDao);
    }
}
