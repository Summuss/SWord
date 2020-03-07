package top.summus.sword.room;

import android.content.Context;

import androidx.room.Room;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import top.summus.sword.room.dao.BookNodeDao;
import top.summus.sword.room.dao.DeleteRecordDao;
import top.summus.sword.room.dao.MeaningDao;
import top.summus.sword.room.dao.SentenceDao;
import top.summus.sword.room.dao.WordBookNodeJoinDao;
import top.summus.sword.room.dao.WordDao;
import top.summus.sword.room.service.BookNodeRoomService;
import top.summus.sword.room.service.DeleteRecordRoomService;
import top.summus.sword.room.service.MeaningRoomService;
import top.summus.sword.room.service.SentenceRoomService;
import top.summus.sword.room.service.WordBookNodeJoinRoomService;
import top.summus.sword.room.service.WordRoomService;

@Module
public class RoomModule {
    @Singleton
    @Provides
    public SWordDatabase provideDatabase(Context context) {
        return Room.databaseBuilder(context, SWordDatabase.class, "sword")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
    }

    @Singleton
    @Provides
    public BookNodeDao provideBookNodeDao(SWordDatabase database) {
        return database.getBookNodeDao();
    }

    @Singleton
    @Provides
    public BookNodeRoomService provideBookNodeRoomService(BookNodeDao bookNodeDao, DeleteRecordRoomService recordRoomService) {
        return new BookNodeRoomService(bookNodeDao, recordRoomService);
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

    @Singleton
    @Provides
    public WordDao providesWordDao(SWordDatabase database) {
        return database.getWordDao();
    }

    @Singleton
    @Provides
    public WordRoomService providesWordRoomService(WordDao wordDao, WordBookNodeJoinRoomService wordBookNodeJoinRoomService) {
        return new WordRoomService(wordDao, wordBookNodeJoinRoomService);
    }

    @Singleton
    @Provides
    public WordBookNodeJoinDao provideWordBookNodeJoinDao(SWordDatabase database) {
        return database.getWordBookNodeRelationDao();
    }

    @Singleton
    @Provides
    public WordBookNodeJoinRoomService provideWordBookNodeJoinService(WordBookNodeJoinDao wordBookNodeJoinDao) {
        return new WordBookNodeJoinRoomService(wordBookNodeJoinDao);
    }

    @Singleton
    @Provides
    public MeaningDao provideMeaningDao(SWordDatabase database) {
        return database.getMeaningDao();
    }

    @Singleton
    @Provides
    public MeaningRoomService provideMeaningRoomService(MeaningDao meaningDao) {
        return new MeaningRoomService(meaningDao);
    }

    @Singleton
    @Provides
    public SentenceDao provideSentenceDao(SWordDatabase database) {
        return database.getSentenceDao();
    }

    @Singleton
    @Provides
    public SentenceRoomService provideSentenceRoomService(SentenceDao sentenceDao) {
        return new SentenceRoomService(sentenceDao);
    }

}
