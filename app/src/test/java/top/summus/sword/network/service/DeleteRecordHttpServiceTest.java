package top.summus.sword.network.service;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;


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
        daggerContainer.database.getWordDao().insert(Word.builder().build()).subscribe();
        daggerContainer.database.getWordDao().selectByPrimary(5).subscribe((word, throwable) -> {
            System.out.println(word);
        });


    }
}