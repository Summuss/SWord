package top.summus.sword.room.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static androidx.room.ForeignKey.CASCADE;

@Entity(foreignKeys = {@ForeignKey(entity = Meaning.class, parentColumns = "id", childColumns = "meaning_id", onDelete = CASCADE)})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Sentence implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "meaning_id", index = true)
    private long meaningId;

    private String sentence;

    private String interpretation;

    @ColumnInfo(name = "changed_date")
    @Builder.Default
    private Date changedDate = new Date();

    @ColumnInfo(name = "sync_status")
    @Builder.Default
    private int syncStatus = 1;


}
