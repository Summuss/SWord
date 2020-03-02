package top.summus.sword.room.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Entity(
        tableName = "book_node",
        indices = {@Index(value = {"node_name", "node_path", "node_tag"}, unique = true)}
)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class BookNode implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "node_no", defaultValue = "-1")
    @Builder.Default
    private long nodeNo = -1;

    @ColumnInfo(name = "node_name")
    private String nodeName;

    @ColumnInfo(name = "node_path")
    private String nodePath;

    /**
     * 0为folder,1为page
     */
    @ColumnInfo(name = "node_tag")
    private int nodeTag;

    @ColumnInfo(name = "node_changed_date")
    private Date nodeChangedDate;

    @ColumnInfo(name = "sync_status", defaultValue = "1")
    @Builder.Default
    private int syncStatus = 1;

}

