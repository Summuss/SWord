package top.summus.sword.room.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Calendar;
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

    @ColumnInfo(name = "review_date")
    private Date reviewDate;


    @ColumnInfo(name = "sync_status")
    @Builder.Default
    private int syncStatus = 1;

    public boolean know() {
        proficiency += 2 * (12 - difficulty - priority);
        if (proficiency >= 100) {
            proficiency = 100;
            return true;
        } else {
            updateReviewDate();
            return false;
        }
    }

    public void forget() {
        proficiency = (int) (0.3 * proficiency);
    }

    public void ambiguous() {
        proficiency = (int) (0.6 * proficiency);
    }


    private void updateReviewDate() {
        int interval = fibonacciSqe(proficiency / 10);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(reviewDate);
        calendar.add(Calendar.DAY_OF_YEAR, interval);
        reviewDate = calendar.getTime();

    }

    private int fibonacciSqe(int n) {
        if (n < 1) {
            return 0;
        } else if (n == 1 || n == 2) {
            return 1;
        }
        int res = 1;
        int pre = 1;
        int temp = 0;
        for (int i = 3; i < n; i++) {
            temp = res;
            res = pre + res;
            pre = temp;
        }
        return res;
    }

}
