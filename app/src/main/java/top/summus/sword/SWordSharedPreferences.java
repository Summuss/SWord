package top.summus.sword;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

public class SWordSharedPreferences {
    private static final String TAG = "SingletonInit";

    private SharedPreferences sharedPreferences;

    @Getter
    private Date lastSyncTime;

    @Getter
    private long timeGap;

    public SWordSharedPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        if (sharedPreferences.contains("lastSyncTime")) {
            long mills = sharedPreferences.getLong("lastSyncTime", 0);
            lastSyncTime = new Date(mills);
        } else {
            lastSyncTime = new Date();
            setLastSyncTime(lastSyncTime);
        }
        if (sharedPreferences.contains("timeGap")) {
            timeGap = sharedPreferences.getLong("timeGap", 0);
            Log.i(TAG, "SWordSharedPreferences: load timeGap " + timeGap);
        } else {
            Log.i(TAG, "SWordSharedPreferences: load time gap ,not exist");


        }
    }


    public void setLastSyncTime(Date date) {
        lastSyncTime = date;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong("lastSyncTime", date.getTime());
        editor.apply();
    }

    public void setTimeGap(long gap) {
        timeGap = gap;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong("timeGap", timeGap);
        editor.apply();
    }


}
