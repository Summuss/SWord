//package top.summus.sword.repository;
//
//import android.annotation.SuppressLint;
//import android.content.Context;
//import android.util.Log;
//
//import androidx.lifecycle.LiveData;
//
//import java.lang.reflect.Array;
//import java.util.List;
//
//import io.reactivex.schedulers.Schedulers;
//import top.summus.sword.SWordDatabase;
//import top.summus.sword.dao.BookNodeDao;
//import top.summus.sword.entity.BookNode;
//
//@SuppressLint("CheckResult")
//public class BookNodeRepository {
//    private static final String TAG = "BookNodeRepository";
//    private static BookNodeDao bookNodeDao = SWordDatabase.getInstance().getBookNodeDao();
//
//
////    public static void insert(BookNode... bookNodes) {
////        bookNodeDao.insert(bookNodes).subscribeOn(Schedulers.io())
////                .subscribe(
////                        () -> {
////                            Log.i(TAG, "insert: successfully");
////                        },
////                        (throwable -> {
////                            throwable.printStackTrace();
////
////                            Log.i(TAG, "insert: error");
////                        }));
////    }
//
//    public static List<BookNode> selectByPath(String path) {
////        List<BookNode> result=null;
////        bookNodeDao.selectByPath(path).subscribeOn(Schedulers.io())
////                .subscribe(bookNodes -> {
////                   bookNodes
////                })
////        return bookNodeDao.selectByPath(path);
//        return null;
//
//    }
//
//
//}
