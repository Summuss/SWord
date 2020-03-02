package top.summus.sword.room.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(  tableName = "delete_record",
        indices = {@Index(value = {"table_no", "item_no"}, unique = true)})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeleteRecord {
    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "table_no")
    private int tableNo;

    @ColumnInfo(name = "item_no")
    private long itemNo;



}
