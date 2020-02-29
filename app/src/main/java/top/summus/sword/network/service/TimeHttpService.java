package top.summus.sword.network.service;

import android.annotation.SuppressLint;
import android.util.Log;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import lombok.AllArgsConstructor;

import top.summus.sword.SWordSharedPreferences;
import top.summus.sword.network.api.TimeApi;
import top.summus.sword.util.DateFormatUtil;

@AllArgsConstructor
public class TimeHttpService {

    private static final String TAG = "TimeHttpService";


    TimeApi timeApi;

    SWordSharedPreferences sharedPreferences;

    @SuppressLint("CheckResult")
    public void timeCorrect(Consumer<Throwable> callback) {

        final long mills = System.currentTimeMillis();

        timeApi.getTime().subscribeOn(Schedulers.io())
                .doOnNext(voidResponse -> {
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

                        Log.i(TAG, "[timeCorrect]  " + "write gap to local file ");
                    } else {
                        Log.i(TAG, "[timeCorrect]  " + " get wrong response statusCode " + voidResponse.code());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        voidResponse -> {
                            if (callback != null) {
                                callback.accept(null);
                            }
                        },
                        throwable -> {
                            Log.e(TAG, "[timeCorrect]  error!!", throwable);
                            if (callback != null) {
                                callback.accept(throwable);
                            }

                        }
                );

    }

}

