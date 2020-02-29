package top.summus.sword.network.service;

import android.annotation.SuppressLint;
import android.util.Log;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.functions.BiConsumer;
import lombok.AllArgsConstructor;
import okhttp3.Headers;
import top.summus.sword.SWordSharedPreferences;
import top.summus.sword.entity.BookNode;
import top.summus.sword.network.api.BookNodeApi;
import top.summus.sword.room.service.BookNodeRoomService;

import static top.summus.sword.util.DateFormatUtil.parseDateToString;

@AllArgsConstructor
@SuppressLint("CheckResult")
public class BookNodeHttpService {

    private static final String TAG = "BookNodeHttpService";


    @Inject
    BookNodeApi bookNodeApi;

    @Inject
    BookNodeRoomService bookNodeRoomService;

    @Inject
    SWordSharedPreferences sharedPreferences;

    public void downloadBookNodes(Consumer<Throwable> callback) {
        Log.i(TAG, "[download]  start download unsynced");
//        String lastSycnedDate = parseDateToString(new Date(119,2,16,12,59,58));
        String lastSycnedDate = parseDateToString(sharedPreferences.getBookNodeLastPullTime());
        Log.i(TAG, "[download]  lastSyncTime:" + lastSycnedDate);
        bookNodeApi.downLoadUnSynced(lastSycnedDate)
                .subscribeOn(Schedulers.io())
                .doOnNext(listResponse -> {
                    if (listResponse.isSuccessful()) {
                        Log.i(TAG, "[download]  response statusCode is ok");
                        List<BookNode> bookNodes = listResponse.body();
                        for (BookNode bookNode : bookNodes) {
                            List<BookNode> localNodes = bookNodeRoomService.selectByNo(bookNode.getNodeNo());
                            if (!localNodes.isEmpty()) {
                                Log.i(TAG, "[download]  nodeNo" + bookNode.getNodeNo() + " exist in local");
                                BookNode selected = localNodes.get(0);
                                if (bookNode.getNodeChangedDate().getTime() > selected.getNodeChangedDate().getTime()) {
                                    Log.i(TAG, "[download]  nodeNo" + bookNode.getNodeNo() + "  latter than local, updateSync");
                                    bookNode.setId(selected.getId());
                                    bookNode.setSyncStatus(0);
                                    bookNodeRoomService.update(bookNode);
                                } else {
                                    Log.i(TAG, "[download]  nodeNo" + bookNode.getNodeNo() + "  not latter than local, no operation");
                                }
                            } else {
                                Log.i(TAG, "[download] nodeNo" + bookNode.getNodeNo() + "  don't exist in local, insert");
                                bookNode.setSyncStatus(0);
                                bookNodeRoomService.insert(bookNode);
                            }

                        }
                        Log.i(TAG, "[download] finished");
                        Headers headers = listResponse.headers();
                        System.out.println(listResponse.raw());
                        sharedPreferences.setBookNodeLastPullTime(headers.getDate("BookNodePullTime"));

                    } else {
                        Log.e(TAG, "[download]  " + "response statusCode is error,statusCode=" + listResponse.code());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        listResponse -> callback.accept(null),
                        throwable -> {
                            Log.e(TAG, "[download]  " + "fatal error", throwable);
                            callback.accept(throwable);
                        }
                );
    }


}
