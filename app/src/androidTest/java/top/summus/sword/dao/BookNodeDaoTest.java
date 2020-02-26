package top.summus.sword.dao;

import android.content.Context;
import android.util.Log;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;
import java.util.List;

import io.reactivex.schedulers.Schedulers;
import top.summus.sword.SWordDatabase;
import top.summus.sword.entity.BookNode;

import static org.junit.Assert.*;

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
        BookNode bookNode1 = BookNode.builder().nodeName("test").nodeNo(3).nodePath("/").nodeTag(1).nodeChangedDate(new Date())
                .build();

//        bookNodeDao.insert1(bookNode, bookNode1).subscribe((longs, throwable) -> {
//            System.out.println(longs);
//            if (throwable!=null){
//
//                throwable.printStackTrace();
//            }
//        });

        List<BookNode> bookNodes = bookNodeDao.getAll1();
        System.out.println(bookNodes.size());




        if (bookNodes == null) {
//            System.out.println("null");
            Log.i(TAG, "insert: null");

        } else {
            System.out.println(bookNodes);
        }

    }

    @Test
    public void getAll() {
    }

    @Test
    public void selectByPath() {
    }
}