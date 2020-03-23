package top.summus.sword.room.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(indices = {@Index(value = "content", unique = true)})
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Word implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "no")
    @Builder.Default
    private long no = -1;

    @ColumnInfo(name = "content")
    private String content;

    @ColumnInfo(name = "pronunciation")
    private String pronunciation;

    @ColumnInfo(name = "tone")
    private int tone;

    @ColumnInfo(name = "proficiency")
    private int proficiency;

    @ColumnInfo(name = "priority")
    private int priority;

    @ColumnInfo(name = "difficulty")
    private int difficulty;

    @ColumnInfo(name = "tag")
    private int tag;

    @ColumnInfo(name = "changed_date")
    @Builder.Default
    private Date changedDate = new Date();


    @ColumnInfo(name = "sync_status")
    @Builder.Default
    private int syncStatus = 1;


}
