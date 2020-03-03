package top.summus.sword.network.service;

import android.annotation.SuppressLint;
import android.util.Log;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import lombok.AllArgsConstructor;
import retrofit2.Response;
import top.summus.sword.SWordSharedPreferences;
import top.summus.sword.room.entity.BookNode;
import top.summus.sword.exception.WrongStatusCodeException;
import top.summus.sword.network.api.BookNodeApi;
import top.summus.sword.room.service.BookNodeRoomService;
import top.summus.sword.util.Box;

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

    @Inject
    TimeHttpService timeHttpService;


    public Observable<BookNode> downloadBookNodes() {

        Log.i(TAG, "[download]  start download unsynced");
        String lastSycnedDate = parseDateToString(new Date(119, 2, 16, 12, 59, 58));
//        String lastSycnedDate = parseDateToString(sharedPreferences.getBookNodeLastPullTime());
        Log.i(TAG, "[download]  lastSyncTime:" + lastSycnedDate);
        return bookNodeApi.downLoadUnSynced(lastSycnedDate)
                .flatMap((Function<Response<List<BookNode>>, ObservableSource<BookNode>>) listResponse -> {
                    if (listResponse.isSuccessful()) {
                        Log.i(TAG, "[download]  response statusCode is ok");
                        return Observable.fromIterable(listResponse.body());
                    } else {
                        Log.e(TAG, "[download]  " + "response statusCode is error,statusCode=" + listResponse.code());
                        throw new WrongStatusCodeException("wrong status code " + listResponse.code());
                    }
                })
                .doOnNext(bookNode -> {

                    List<BookNode> localNodes = bookNodeRoomService.selectByNoSync(bookNode.getNodeNo());
                    if (!localNodes.isEmpty()) {
                        Log.i(TAG, "[download]  nodeNo" + bookNode.getNodeNo() + " exist in local");
                        BookNode selected = localNodes.get(0);
                        if (bookNode.getNodeChangedDate().getTime() > selected.getNodeChangedDate().getTime()) {
                            Log.i(TAG, "[download]  nodeNo" + bookNode.getNodeNo() + "  latter than local, updateSync");
                            bookNode.setId(selected.getId());
                            bookNode.setSyncStatus(0);
                            bookNodeRoomService.updateSync(bookNode);
                        } else {
                            Log.i(TAG, "[download]  nodeNo" + bookNode.getNodeNo() + "  not latter than local, no operation");
                        }
                    } else {
                        Log.i(TAG, "[download] nodeNo" + bookNode.getNodeNo() + "  don't exist in local, insert");
                        bookNode.setSyncStatus(0);
                        bookNodeRoomService.insertSync(bookNode);
                    }

                })
                .doOnError(throwable -> Log.e(TAG, "downloadBookNodes: ", throwable))
                ;
    }

    public Observable<List<Integer>> downloadAllNodeNo() {
//        Date deleteRecordLastSyncTime = sharedPreferences.getDeleteRecordLastSyncTime();
        Date deleteRecordLastSyncTime = new Date(1483147873000L);

        Box<Date> responseDate = new Box<>();

        return bookNodeApi.getNodeNo(parseDateToString(deleteRecordLastSyncTime))
                .map(listResponse -> {
                    if (listResponse.isSuccessful()) {
                        Log.i(TAG, "downloadAllNodeNo get right status code");
                        responseDate.setValue(listResponse.headers().getDate("Date"));
                        return listResponse.body();
                    } else {
                        Log.e(TAG, "downloadAllNodeNo get wrong status code");
                        throw new WrongStatusCodeException(listResponse.code() + "");
                    }
                })
                .doOnNext(integers -> {
                    List<Long> allNodeNo = bookNodeRoomService.selectAllNodeNoSync();
                    for (Long aLong : allNodeNo) {
                        if (!integers.contains(Integer.valueOf(aLong + ""))) {
                            Log.i(TAG, "downloadAllNodeNo: delete Node which nodeNo=" + aLong);
                            bookNodeRoomService.deleteByNodeNo(aLong);
                        }
                    }
                })
                .doOnComplete(() -> {
                    sharedPreferences.setDeleteRecordLastSyncTime(responseDate.getValue());
                });
    }


    public Observable<BookNode> uploadBookNodes() {

        Observable<BookNode> postBookNodesObservable = Observable.create((ObservableOnSubscribe<List<BookNode>>) emitter -> {
            List<BookNode> insertBookNodes = bookNodeRoomService.selectToBePostedSync();
            emitter.onNext(insertBookNodes);
            emitter.onComplete();
        })
                .flatMap((Function<List<BookNode>, ObservableSource<BookNode>>) Observable::fromIterable)
                .doOnNext(bookNode -> {
                    bookNodeApi.postBookNode(bookNode).subscribe((integerResponse, throwable) -> {
                        if (integerResponse != null) {

                            if (integerResponse.isSuccessful()) {
                                Log.i(TAG, "[post]  " + "nodeId" + bookNode.getId() + " get right response statusCode");
                                bookNode.setNodeNo(integerResponse.body());
                                bookNode.setSyncStatus(0);
                                bookNodeRoomService.updateSync(bookNode);
                                Log.i(TAG, "[post]  " + "post nodeId" + bookNode.getId() + "  finished, get no" + integerResponse.body());

                            } else {
                                Log.e(TAG, "[post]  " + "nodeId" + bookNode.getId() + " get error response statusCode"
                                        , new WrongStatusCodeException(integerResponse.code() + ""));

                            }

                        }
                        if (throwable != null) {
                            Log.e(TAG, "[post]  ", throwable);
                        }

                    });
                });

        Observable<BookNode> patchBookNodesObservable = Observable.create((ObservableOnSubscribe<List<BookNode>>) emitter -> {
            List<BookNode> patchedBookNode = bookNodeRoomService.selectToBePatchedSync();
            emitter.onNext(patchedBookNode);
            emitter.onComplete();
        })
                .flatMap((Function<List<BookNode>, ObservableSource<BookNode>>) Observable::fromIterable)
                .doOnNext(bookNode -> {
                    bookNodeApi.patchBookNode(bookNode).subscribe((integerResponse, throwable) -> {
                        if (integerResponse != null) {
                            if (integerResponse.isSuccessful()) {
                                Log.i(TAG, "[patch]  " + "nodeNo" + bookNode.getNodeNo() + "  get right response statusCode");
                                Log.i(TAG, "[patch]  " + "nodeNo" + bookNode.getNodeNo() + " server's response is " + integerResponse.body());
                                if (integerResponse.body() > 0) {
                                    bookNode.setNodeNo(integerResponse.body());
                                }
                                bookNode.setSyncStatus(0);
                                bookNodeRoomService.updateSync(bookNode);

                            } else {
                                Log.e(TAG, "[patch]  " + "nodeNo" + bookNode.getNodeNo() + " get error response statusCode"
                                        , new WrongStatusCodeException(integerResponse.code() + ""));
                            }
                        }
                        if (throwable != null) {
                            Log.e(TAG, "[patch]  ", throwable);

                        }
                    });
                });

        return Observable.concat(postBookNodesObservable, patchBookNodesObservable);


    }

    public Completable syncBookNodes() {

        return Completable.create(emitter ->
                downloadBookNodes().doFinally(() -> {
                    uploadBookNodes().doFinally(() -> {
                        downloadAllNodeNo().doFinally(emitter::onComplete).subscribe(integers -> {
                        }, throwable -> Log.e(TAG, "subscribe: ", throwable));
                    }).subscribe(bookNode -> {
                    }, throwable -> Log.e(TAG, "subscribe: ", throwable));
                }).subscribe(bookNode -> {
                }, throwable -> Log.e(TAG, "subscribe: ", throwable)));


    }


}
