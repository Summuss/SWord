package top.summus.sword.room.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import top.summus.sword.entity.BookNode;

@Dao
public interface BookNodeRoomDao {


    @Insert
    Single<List<Long>> insert(BookNode... bookNodes);

    @Insert
    List<Long> insertSynced(BookNode... bookNodes);

    @Query("SELECT * FROM book_node")
    List<BookNode> getAllBySync();

    @Update
    void updateSynced(BookNode... bookNodes);

    @Delete
    Completable delete(BookNode... bookNodes);


    @Query("SELECT * FROM book_node WHERE node_path = :path ORDER BY node_tag,node_name")
    Single<List<BookNode>> selectByPath(String path);

    @Query("SELECT * FROM book_node WHERE node_path = :path ORDER BY node_tag,node_name")
    List<BookNode> selectByPathBySync(String path);

    @Query("SELECT COUNT(*) FROM book_node WHERE node_no= :no")
    long selectCountByNoSynced(long no);

    @Query("SELECT * FROM book_node WHERE node_no=:no ")
    List<BookNode> selectByNoSynced(long no);
}
