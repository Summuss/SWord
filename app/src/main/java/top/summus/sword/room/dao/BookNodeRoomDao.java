package top.summus.sword.room.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;
import top.summus.sword.entity.BookNode;

@Dao
public interface BookNodeRoomDao {


    @Insert
    Single<Long> insert(BookNode bookNodes);

    @Insert
    long insertSync(BookNode bookNode);


    @Query("SELECT * FROM book_node")
    List<BookNode> getAllBySync();

    @Update
    void updateSync(BookNode... bookNodes);

    @Delete
    Completable delete(BookNode... bookNodes);


    @Query("SELECT * FROM book_node WHERE node_path = :path ORDER BY node_tag,node_name")
    Single<List<BookNode>> selectByPath(String path);

    @Query("SELECT * FROM book_node WHERE node_path = :path ORDER BY node_tag,node_name")
    List<BookNode> selectByPathBySync(String path);


    @Query("SELECT COUNT(*) FROM book_node WHERE node_no= :no")
    long selectCountByNoSync(long no);

    @Query("SELECT * FROM book_node WHERE id=:id")
    BookNode selectByPrimary(long id);

    @Query("UPDATE book_node SET node_no=:no WHERE id=:primaryKey")
    void setNodeNoByPrimary(long primaryKey,long no);


    @Query("SELECT * FROM book_node WHERE node_no=:no ")
    Single<List<BookNode>> selectByNo(long no);

    @Query("SELECT * FROM book_node WHERE node_no=:no ")
    List<BookNode> selectByNoSync(long no);

    @Query("SELECT * FROM book_node WHERE node_no!=-1 AND sync_status=1")
    List<BookNode> selectToBePatchedSynced();

    @Query("SELECT * FROM book_node WHERE node_no=-1")
    List<BookNode> selectToBePostedSynced();

}
