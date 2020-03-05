package top.summus.sword.room.dao;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;

import top.summus.sword.room.SWordDatabase;
import top.summus.sword.room.entity.BookNode;

@RunWith(AndroidJUnit4.class)
public class BookNodeDaoTest {

    private static final String TAG = "BookNodeDaoTest";
    private BookNodeDao bookNodeDao;
    private SWordDatabase database;

    @Before
    public void setUp() throws Exception {
        Context context = ApplicationProvider.getApplicationContext();
        database = Room.inMemoryDatabaseBuilder(context, SWordDatabase.class).build();
        bookNodeDao = database.getBookNodeDao();
    }

    @After
    public void close() {
        database.close();
    }

    @Test
    public void insert() {
        BookNode bookNode = BookNode.builder().nodeName("test").nodeNo(3).nodePath("/").nodeTag(1).nodeChangedDate(new Date())
                .build();
        BookNode bookNode1 = BookNode.builder().nodeName("test3").nodeNo(3).nodePath("/").nodeTag(1).nodeChangedDate(new Date())

                .build();
//        bookNodeDao.insertSynced(bookNode, bookNode1);

        long num = bookNodeDao.selectCountByNoSync(4);


//        bookNodeDao.insert1(bookNode, bookNode1).subscribe((longs, throwable) -> {
//            System.out.println(longs);
//            if (throwable!=null){
//
//                throwable.printStackTrace();
//            }
//        });







    }

    @Test
    public void getAll() {
    }

    @Test
    public void selectByPath() {
    }
}