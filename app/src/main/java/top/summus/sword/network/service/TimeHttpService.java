package top.summus.sword.network.service;

import android.annotation.SuppressLint;
import android.util.Log;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.schedulers.Schedulers;
import lombok.NoArgsConstructor;
import okhttp3.OkHttpClient;
import top.summus.sword.SWordApplication;
import top.summus.sword.SWordSharedPreferences;
import top.summus.sword.network.api.TimeApi;
import top.summus.sword.util.DateFormatUtil;

@NoArgsConstructor
public class TimeHttpService {

    private static final String TAG = "TimeHttpService";

    private TimeCallback callback;

    @Inject
    TimeApi timeApi;

    @Inject
    SWordSharedPreferences sharedPreferences;

    public TimeHttpService(TimeCallback callback) {
        this.callback = callback;
        SWordApplication.getAppComponent().inject(this);
    }


    @SuppressLint("CheckResult")
    public void timeCorrect() {
        final OkHttpClient client = new OkHttpClient.Builder().
                readTimeout(100, TimeUnit.MILLISECONDS)
                .build();

        final long mills = System.currentTimeMillis();

        timeApi.getTime().subscribeOn(Schedulers.io())
                .subscribe(
                        voidResponse -> {
                            long currentMills = System.currentTimeMillis();
                            long delay = currentMills - mills;
                            Log.i(TAG, "[timeCorrect]  " + "delay " + delay);
                            if (voidResponse.isSuccessful()) {
                                Log.i(TAG, "[timeCorrect]  " + "get right response statusCode");
                                Log.i(TAG, "[timeCorrect]  " + "get server time: " + DateFormatUtil.parseDateToString(voidResponse.headers().getDate("Date")));
                                ;
                                long serverMills = voidResponse.headers().getDate("Date").getTime();
                                long gap = serverMills - delay - currentMills;

                                Log.i(TAG, "[timeCorrect]  " + "last gap is" + sharedPreferences.getTimeGap());
                                Log.i(TAG, "[timeCorrect]  " + "calculated gap is " + gap);
                                Log.i(TAG, "[timeCorrect]  " + "minus last gap is " + (gap - sharedPreferences.getTimeGap()));

                                sharedPreferences.setTimeGap(gap);
                                if (callback != null) {
                                    callback.onTimeCorrectFinished();
                                }
                                Log.i(TAG, "[timeCorrect]  " + "write gap to local file ");
                            } else {
                                Log.i(TAG, "[timeCorrect]  " + " get wrong response statusCode");
                            }
                        },
                        throwable -> Log.e(TAG, "[timeCorrect]  error!!", throwable)
                );

    }

    public interface TimeCallback {
        void onTimeCorrectFinished();
    }
}

