package top.summus.sword;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.Date;

import lombok.Getter;

public class SWordSharedPreferences {
    private static final String TAG = "SingletonInit";

    private SharedPreferences sharedPreferences;

    @Getter
    private Date bookNodeLastPullTime;

    @Getter
    private long timeGap;

    @Getter
    private Date deleteRecordLastSyncTime;

    public SWordSharedPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        if (sharedPreferences.contains("bookNodeLastPullTime")) {
            long mills = sharedPreferences.getLong("bookNodeLastPullTime", 0);
            bookNodeLastPullTime = new Date(mills);
        } else {
            bookNodeLastPullTime = new Date(100, 1, 1);
            setBookNodeLastPullTime(bookNodeLastPullTime);
        }
        if (sharedPreferences.contains("timeGap")) {
            timeGap = sharedPreferences.getLong("timeGap", 0);
            Log.i(TAG, "SWordSharedPreferences: load timeGap " + timeGap);
        } else {
            Log.i(TAG, "SWordSharedPreferences: load time gap ,not exist");

        }
        if(sharedPreferences.contains("deleteRecordLastSyncTime")){
            deleteRecordLastSyncTime=new Date(sharedPreferences.getLong("deleteRecordLastSyncTime",0));

        }else {
            deleteRecordLastSyncTime = new Date(100, 1, 1);
            setDeleteRecordLastSyncTime(deleteRecordLastSyncTime);

        }
    }


    public void setBookNodeLastPullTime(Date date) {
        bookNodeLastPullTime = date;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong("bookNodeLastPullTime", date.getTime());
        editor.apply();
    }

    public void setTimeGap(long gap) {
        timeGap = gap;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong("timeGap", timeGap);
        editor.apply();
    }

    public void setDeleteRecordLastSyncTime(Date date){

        deleteRecordLastSyncTime = date;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong("deleteRecordLastSyncTime", date.getTime());
        editor.apply();
    }

}
