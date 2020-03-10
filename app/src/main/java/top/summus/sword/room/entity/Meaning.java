package top.summus.sword.room.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static androidx.room.ForeignKey.CASCADE;

@Entity(foreignKeys = {@ForeignKey(entity = Word.class, parentColumns = "id", childColumns = "word_id", onDelete = CASCADE)})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Meaning {
    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "word_id", index = true)
    private long wordId;

    @ColumnInfo(name = "word_class")
    private int wordClass;

    @ColumnInfo(name = "meaning")
    private String meaning;

    @ColumnInfo(name = "changed_date")
    private Date changedDate;

    @ColumnInfo(name = "sync_status")
    @Builder.Default
    private int syncStatus = 1;

}
