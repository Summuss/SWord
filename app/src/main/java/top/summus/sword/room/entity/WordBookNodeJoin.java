package top.summus.sword.room.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "word_book_node_join",
        indices = {@Index(value = {"word_id", "book_node_id"}, unique = true)},
        foreignKeys = {@ForeignKey(entity = Word.class, parentColumns = "id", childColumns = "word_id", onDelete = CASCADE),
                @ForeignKey(entity = BookNode.class, parentColumns = "id", childColumns = "book_node_id", onDelete = CASCADE)}

)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WordBookNodeJoin {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @Builder.Default
    @ColumnInfo(name = "no")
    private long no = -1;

    @ColumnInfo(name = "word_id", index = true)
    private long wordId;

    @ColumnInfo(name = "book_node_id", index = true)
    private long bookNodeId;

    @ColumnInfo(name = "sync_status")
    @Builder.Default
    private int syncStatus = 1;


}
