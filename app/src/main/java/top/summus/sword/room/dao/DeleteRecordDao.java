package top.summus.sword.room.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import top.summus.sword.room.entity.DeleteRecord;

@Dao
public interface DeleteRecordDao {
    @Insert
    Single<Long> insert(DeleteRecord record);

    @Delete
    Completable delete(DeleteRecord record);

    @Query("SELECT * FROM delete_record WHERE table_no=:table")
    List<DeleteRecord> selectByTable(int table);


    enum Table {
        /**
         *
         */
        BOOK_NODE(1),
        WORD_TABLE(2),
        WORD_RECORD(3);
        private int value;

        Table(int value) {
            this.value = value;
        }

        public int value() {
            return value;
        }

    }

}
