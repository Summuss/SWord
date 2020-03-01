package top.summus.sword.network.service;

import android.annotation.SuppressLint;
import android.util.Log;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Semaphore;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import lombok.AllArgsConstructor;
import okhttp3.Headers;
import retrofit2.Response;
import top.summus.sword.SWordSharedPreferences;
import top.summus.sword.entity.BookNode;
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


    public Observable<BookNode> downloadBookNodes() {

        Log.i(TAG, "[download]  start download unsynced");
        String lastSycnedDate = parseDateToString(new Date(119,2,16,12,59,58));
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
                        bookNodeRoomService.insert(bookNode).subscribe();
                    }

                })
                .doOnError(throwable -> Log.e(TAG, "downloadBookNodes: ",throwable ))
                .observeOn(AndroidSchedulers.mainThread());
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

    public void uploadBookNodes(Consumer<Throwable> callback, Semaphore semaphore) {
        Semaphore localSema = new Semaphore(-1, true);
        Single.create((SingleOnSubscribe<List<BookNode>>) emitter -> {
            List<BookNode> insertBookNodes = bookNodeRoomService.selectToBePosted();
            emitter.onSuccess(insertBookNodes);
        }).subscribeOn(Schedulers.io())
                .subscribe((insertBookNodes, throwable) -> {
                    if (insertBookNodes != null) {
                        Observable.fromIterable(insertBookNodes)
                                .map(bookNode -> bookNodeApi.postBookNode(bookNode));
//                                .zipWith(Observable.fromIterable(insertBookNodes), new BiFunction<Single<Response<Integer>>, BookNode, Object>() {
//                                });

                    }
                    if (throwable != null) {
                        Log.e(TAG, "selectToBePosted ", throwable);
                        localSema.release();
                    }
                });

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
