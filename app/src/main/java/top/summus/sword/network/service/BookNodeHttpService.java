package top.summus.sword.network.service;

import android.annotation.SuppressLint;
import android.util.Log;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Semaphore;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import lombok.AllArgsConstructor;
import okhttp3.Headers;
import retrofit2.Response;
import top.summus.sword.SWordSharedPreferences;
import top.summus.sword.room.entity.BookNode;
import top.summus.sword.exception.WrongStatusCodeException;
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

    @Inject
    TimeHttpService timeHttpService;


    public Observable<BookNode> downloadBookNodes() {

        Log.i(TAG, "[download]  start download unsynced");
        String lastSycnedDate = parseDateToString(new Date(119, 2, 16, 12, 59, 58));
//        String lastSycnedDate = parseDateToString(sharedPreferences.getBookNodeLastPullTime());
        Log.i(TAG, "[download]  lastSyncTime:" + lastSycnedDate);
        return bookNodeApi.downLoadUnSynced(lastSycnedDate).subscribeOn(Schedulers.io())
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

    public void downloadBookNodes(Consumer<Throwable> callback, Semaphore semaphore) {
        Log.i(TAG, "[download]  start download unsynced");
        String lastSycnedDate = parseDateToString(new Date(119, 2, 16, 12, 59, 58));
//        String lastSycnedDate = parseDateToString(sharedPreferences.getBookNodeLastPullTime());
        Log.i(TAG, "[download]  lastSyncTime:" + lastSycnedDate);
        bookNodeApi.downLoadUnSynced(lastSycnedDate)
                .subscribeOn(Schedulers.io())
                .doOnNext(listResponse -> {
                    if (listResponse.isSuccessful()) {
                        Log.i(TAG, "[download]  response statusCode is ok");
                        List<BookNode> bookNodes = listResponse.body();
                        for (BookNode bookNode : bookNodes) {
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
                                bookNodeRoomService.insert(bookNode).subscribe();

                            }

                        }
                        Log.i(TAG, "[download] finished");
                        Headers headers = listResponse.headers();
                        System.out.println(listResponse.raw());
                        sharedPreferences.setBookNodeLastPullTime(headers.getDate("BookNodePullTime"));

                    } else {
                        Log.e(TAG, "[download]  " + "response statusCode is error,statusCode=" + listResponse.code());
                    }
                    semaphore.release();
                })
                .doOnError(throwable -> {
                    semaphore.release();
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        listResponse -> {
                            if (callback != null) {
                                callback.accept(null);
                            }
                        },
                        throwable -> {
                            Log.e(TAG, "[download]  " + "fatal error", throwable);
                            if (callback != null) {
                                callback.accept(throwable);
                            }
                        }
                );
    }

    public Observable<BookNode> uploadBookNodes() {

        Observable<BookNode> postBookNodesObservable = Observable.create((ObservableOnSubscribe<List<BookNode>>) emitter -> {
            List<BookNode> insertBookNodes = bookNodeRoomService.selectToBePostedSync();
            emitter.onNext(insertBookNodes);
            emitter.onComplete();
        }).subscribeOn(Schedulers.io())
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
        }).observeOn(Schedulers.io())
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

        return Observable.concat(postBookNodesObservable,patchBookNodesObservable).observeOn(AndroidSchedulers.mainThread());


    }

    public void syncBookNodes(Consumer<Throwable> callback) {
        Semaphore semaphore = new Semaphore(-1, true);
        Throwable[] throwables = new Throwable[2];
        timeHttpService.timeCorrect(throwable -> throwables[0] = throwable, semaphore);
        downloadBookNodes(throwable -> throwables[1] = throwable, semaphore);
//        Observable.create(emitter -> {
//            semaphore.acquire();
//            if (throwables[0] != null) {
//                emitter.onError(throwables[0]);
//            } else if (throwables[1] != null) {
//                emitter.onError(throwables[1]);
//            } else {
//                emitter.onComplete();
//            }
//        }).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe()
        Completable.create(emitter -> {
            semaphore.acquire();
            if (throwables[0] != null) {
                emitter.onError(throwables[0]);
            } else if (throwables[1] != null) {
                emitter.onError(throwables[1]);
            } else {
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                            callback.accept(null);
                        },
                        throwable -> callback.accept(throwable));
//        ((Runnable) () -> {
//            try {
//                semaphore.acquire();
//                Log.i(TAG, "syncBookNodes: " + Thread.currentThread());
//                Log.i(TAG, "syncBookNodes: get sema");
//                if (throwables[0] != null) {
//                    callback.accept(throwables[0]);
//                } else if (throwables[1] != null) {
//                    callback.accept(throwables[1]);
//                } else {
//                    callback.accept(null);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }).run();

    }

}
