package top.summus.sword.room.dao;

import androidx.room.Dao;

import androidx.room.Query;

import java.util.List;

import io.reactivex.Single;
import top.summus.sword.room.entity.BookNode;
import top.summus.sword.room.entity.Word;
import top.summus.sword.room.entity.WordBookNodeJoin;

@Dao
public interface WordBookNodeJoinDao extends BaseDao<WordBookNodeJoin> {

    @Query("SELECT * FROM word_book_node_join WHERE id=:id")
    Single<WordBookNodeJoin> selectByPrimary(long id);

    @Query("SELECT * FROM word INNER JOIN word_book_node_join ON  word_book_node_join.word_id =word.id " +
            "WHERE word_book_node_join.book_node_id=:bookNodeId")
    Single<List<Word>> selectWordsByBookNodeId(long bookNodeId);

    @Query("SELECT * FROM book_node INNER JOIN word_book_node_join ON word_book_node_join.book_node_id=book_node.id " +
            "WHERE word_book_node_join.word_id=:wordId")
    Single<List<BookNode>> selectBookNodesByWordId(long wordId);

}
