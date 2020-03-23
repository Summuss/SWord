package top.summus.sword.network.service;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;


import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import top.summus.sword.SWordApplication;
import top.summus.sword.dagger.DaggerContainer;
import top.summus.sword.room.entity.BookNode;
import top.summus.sword.room.entity.Word;

@RunWith(RobolectricTestRunner.class)
public class DeleteRecordHttpServiceTest {
    private static final String TAG = "DeleteRecordHttpService";
    private DaggerContainer daggerContainer;

    @Before
    public void setUp() throws Exception {
        daggerContainer=SWordApplication.daggerContainer;
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void downloadDeleteRecord() {
        System.out.println("hello");
        Observable.just(1, 2, 3)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    System.out.println("in finally " + Thread.currentThread());
                })
                .subscribe(integer -> {
                    System.out.println("in subscribe " + integer + "  " + Thread.currentThread());
                });


    }
}