package top.summus.sword.room.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "current_study_word",
        foreignKeys = @ForeignKey(entity = Word.class, parentColumns = "id", childColumns = "word_id", onDelete = CASCADE))
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CurrentStudyWord {
    @PrimaryKey
    private long id;

    @ColumnInfo(name = "word_id", index = true)
    private long wordId;

}
