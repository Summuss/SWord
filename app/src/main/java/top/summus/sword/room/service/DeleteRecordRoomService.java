package top.summus.sword.room.service;

import android.annotation.SuppressLint;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;
import lombok.AllArgsConstructor;
import top.summus.sword.room.dao.DeleteRecordDao;
import top.summus.sword.room.entity.DeleteRecord;

@SuppressLint("CheckResult")

@AllArgsConstructor
public class DeleteRecordRoomService {
    @Inject
    DeleteRecordDao deleteRecordDao;

    public Single<Long> insert(DeleteRecordDao.Table table, long itemNo) {
        DeleteRecord record = DeleteRecord.builder().tableNo(table.value()).itemNo(itemNo).build();
        return deleteRecordDao.insert(record);

    }

    public Completable delete(DeleteRecord record) {
        return deleteRecordDao.delete(record);
    }

    public List<DeleteRecord> selectByTableSync(DeleteRecordDao.Table table) {
        return deleteRecordDao.selectByTable(table.value());
    }

}
