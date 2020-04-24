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

@Entity(foreignKeys = {@ForeignKey(entity = Word.class, parentColumns = "id", childColumns = "word_id", onDelete = CASCADE)})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Meaning implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "word_id", index = true)
    private long wordId;

    @ColumnInfo(name = "word_class")
    private int wordClass;

    @ColumnInfo(name = "meaning")
    private String meaning;

    @ColumnInfo(name = "changed_date")
    @Builder.Default
    private Date changedDate = new Date();

    @ColumnInfo(name = "sync_status")
    @Builder.Default
    private int syncStatus = 1;

    public enum WordClass {
        VERB(0), NOUN(1), ADJ(2);

        private int value;

        public int value() {
            return value;
        }

        WordClass(int value) {
            this.value = value;
        }

        public String getWordClass() {
            switch (value) {
                case 0:
                    return "動";
                case 1:
                    return "名";
                case 2:
                    return "形";
                default:
                    throw new UnsupportedOperationException();
            }
        }

        public static WordClass getEnum(int value) {
            switch (value) {
                case 0:
                    return VERB;
                case 1:
                    return NOUN;
                case 2:
                    return ADJ;
                default:
                    throw new UnsupportedOperationException();
            }
        }

    }

}
