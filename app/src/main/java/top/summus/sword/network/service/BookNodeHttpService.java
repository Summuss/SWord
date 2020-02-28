package top.summus.sword.network.service;

import android.annotation.SuppressLint;
import android.util.Log;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.schedulers.Schedulers;
import lombok.NoArgsConstructor;
import okhttp3.Headers;
import top.summus.sword.SWordApplication;
import top.summus.sword.SWordSharedPreferences;
import top.summus.sword.entity.BookNode;
import top.summus.sword.network.api.BookNodeApi;
import top.summus.sword.room.dao.BookNodeRoomDao;

import static top.summus.sword.util.DateFormatUtil.parseDateToString;

@NoArgsConstructor
@SuppressLint("CheckResult")
public class BookNodeHttpService {

    private static final String TAG = "BookNodeHttpService";

    private BookNodeHttpServiceCallback callback;

    @Inject
    BookNodeApi bookNodeApi;

    @Inject
    BookNodeRoomDao bookNodeRoomDao;

    @Inject
    SWordSharedPreferences sharedPreferences;

    public BookNodeHttpService(BookNodeHttpServiceCallback callback) {
        this.callback = callback;
        SWordApplication.getAppComponent().inject(this);
    }

    public void downloadUnSynced() {
        Log.i(TAG, "[download]  start download unsynced");
//        String lastSycnedDate = parseDateToString(new Date(119,2,16,12,59,58));
        String lastSycnedDate = parseDateToString(sharedPreferences.getBookNodeLastPullTime());
        Log.i(TAG, "[download]  lastSyncTime:" + lastSycnedDate);
        bookNodeApi.downLoadUnSynced(lastSycnedDate)
                .subscribeOn(Schedulers.io())
                .subscribe(
                        listResponse -> {
                            if (listResponse.isSuccessful()) {
                                Log.i(TAG, "[download]  response statusCode is ok");
                                List<BookNode> bookNodes = listResponse.body();
                                for (BookNode bookNode : bookNodes) {
                                    List<BookNode> localNodes = bookNodeRoomDao.selectByNoSynced(bookNode.getNodeNo());
                                    bookNode.setSyncStatus(0);

                                    if (!localNodes.isEmpty()) {
                                        Log.i(TAG, "[download]  nodeNo" + bookNode.getNodeNo() + " exist in local");
                                        BookNode selected = localNodes.get(0);
                                        if (bookNode.getNodeChangedDate().getTime() > selected.getNodeChangedDate().getTime()) {
                                            Log.i(TAG, "[download]  nodeNo" + bookNode.getNodeNo() + "  latter than local, updateSynced");
                                            bookNode.setId(selected.getId());
                                            bookNodeRoomDao.updateSynced(bookNode);
                                        } else {
                                            Log.i(TAG, "[download]  nodeNo" + bookNode.getNodeNo() + "  not latter than local, no operation");
                                        }
                                    } else {
                                        Log.i(TAG, "[download] nodeNo" + bookNode.getNodeNo() + "  don't exist in local, insert");
                                        bookNodeRoomDao.insertSynced(bookNode);
                                    }
                                }
                                Log.i(TAG, "[download] finished");
                                Headers headers = listResponse.headers();
                                System.out.println(listResponse.raw());
                                sharedPreferences.setBookNodeLastPullTime(headers.getDate("BookNodePullTime"));
                                if (callback != null) {
                                    callback.onDownLoadBookNodesFinished();
                                }
                            } else {
                                Log.e(TAG, "[download]  " + "response statusCode is error");
                            }
                        },
                        throwable -> Log.e(TAG, "[download]  " + "fatal error", throwable)
                );

    }

    public interface BookNodeHttpServiceCallback {
        void onDownLoadBookNodesFinished();

    }
}
