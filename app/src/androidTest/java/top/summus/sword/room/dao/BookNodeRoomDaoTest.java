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

import top.summus.sword.SWordDatabase;
import top.summus.sword.entity.BookNode;

@RunWith(AndroidJUnit4.class)
public class BookNodeRoomDaoTest {

    private static final String TAG = "BookNodeRoomDaoTest";
    private BookNodeRoomDao bookNodeRoomDao;
    private SWordDatabase database;

    @Before
    public void setUp() throws Exception {
        Context context = ApplicationProvider.getApplicationContext();
        database = Room.inMemoryDatabaseBuilder(context, SWordDatabase.class).build();
        bookNodeRoomDao = database.getBookNodeDao();
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
        bookNodeRoomDao.insertSynced(bookNode, bookNode1);

        long num = bookNodeRoomDao.selectCountByNoSync(4);


//        bookNodeRoomDao.insert1(bookNode, bookNode1).subscribe((longs, throwable) -> {
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