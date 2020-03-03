package top.summus.sword;

import android.util.Log;

import org.junit.Test;

import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.Semaphore;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.BiFunction;
import io.reactivex.schedulers.Schedulers;
import top.summus.sword.room.dao.DeleteRecordDao;
import top.summus.sword.util.Box;

import static org.junit.Assert.*;
import static top.summus.sword.room.dao.DeleteRecordDao.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    private static final String TAG = "ExampleUnitTest";
    public String test() {
        final String s = "hello";
        return s;
    }

    public Observable<Integer> func(){
        return Observable.just(1,2,3)
                .subscribeOn(Schedulers.io());
    }

    public static void main(String[] args) {
    }


}