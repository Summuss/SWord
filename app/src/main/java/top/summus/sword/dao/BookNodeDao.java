package top.summus.sword.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import top.summus.sword.entity.BookNode;

@Dao
public interface BookNodeDao {
    @Insert
    Completable insert(BookNode... bookNodes);

    @Insert
    void insert1(BookNode... bookNodes);


    @Query("SELECT * FROM book_node ORDER BY node_tag,node_name")
    LiveData<List<BookNode>> getAll();

    @Query("SELECT * FROM book_node")
    List<BookNode> getAll1();

    @Update
    void update(BookNode... bookNodes);


    @Query("SELECT * FROM book_node WHERE node_path = :path ORDER BY node_tag,node_name")
    LiveData<List<BookNode>> selectByPath(String path);

}
