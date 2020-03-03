package top.summus.sword.dagger;

import javax.inject.Inject;

import top.summus.sword.SWordApplication;
import top.summus.sword.network.api.BookNodeApi;
import top.summus.sword.network.api.DeleteRecordApi;
import top.summus.sword.network.service.BookNodeHttpService;
import top.summus.sword.network.service.DeleteRecordHttpService;
import top.summus.sword.room.SWordDatabase;
import top.summus.sword.room.dao.BookNodeRoomDao;
import top.summus.sword.room.dao.DeleteRecordDao;
import top.summus.sword.room.service.BookNodeRoomService;
import top.summus.sword.room.service.DeleteRecordRoomService;

public class DaggerContainer {
    @Inject
    public BookNodeRoomDao bookNodeRoomDao;
    @Inject
    public BookNodeRoomService bookNodeRoomService;
    @Inject
    public BookNodeHttpService bookNodeHttpService;

    @Inject
    public DeleteRecordHttpService deleteRecordHttpService;
    @Inject
    public DeleteRecordRoomService deleteRecordRoomService;
    @Inject
    public DeleteRecordDao deleteRecordDao;

    @Inject
    public DeleteRecordApi deleteRecordApi;
    @Inject
    public BookNodeApi bookNodeApi;

    @Inject
    public SWordDatabase database;

    public DaggerContainer(){
        SWordApplication.getAppComponent().inject(this);
    }
}
