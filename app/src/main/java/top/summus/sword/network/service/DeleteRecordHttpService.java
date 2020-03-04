package top.summus.sword.network.service;

import android.annotation.SuppressLint;
import android.util.Log;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Semaphore;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import lombok.AllArgsConstructor;
import retrofit2.Response;
import top.summus.sword.SWordSharedPreferences;
import top.summus.sword.exception.ErrorItem;
import top.summus.sword.exception.WrongStatusCodeException;
import top.summus.sword.network.api.DeleteRecordApi;
import top.summus.sword.room.entity.BookNode;
import top.summus.sword.room.entity.DeleteRecord;
import top.summus.sword.room.service.BookNodeRoomService;
import top.summus.sword.room.service.DeleteRecordRoomService;
import top.summus.sword.util.Box;
import top.summus.sword.util.DateFormatUtil;

import static top.summus.sword.room.dao.DeleteRecordDao.Table.BOOK_NODE;

@SuppressLint("CheckResult")

@AllArgsConstructor
public class DeleteRecordHttpService {
    private static final String TAG = "DeleteRecordHttpService";

    DeleteRecordRoomService deleteRecordRoomService;
    BookNodeRoomService bookNodeRoomService;
    SWordSharedPreferences sharedPreferences;
    TimeHttpService timeHttpService;
    DeleteRecordApi deleteRecordApi;
    ErrorCollectionService errorCollectionService;

    public Observable<DeleteRecord> downloadDeleteRecord() {
        Date deleteRecordLastSyncTime = sharedPreferences.getDeleteRecordLastSyncTime();
//        Date deleteRecordLastSyncTime = new Date(1583147873000L);
        Log.i(TAG, "downloadDeleteRecord: deleteRecordLastSyncTime " + deleteRecordLastSyncTime);
        Box<Date> responseDate = new Box<>();
        return deleteRecordApi.getAll(DateFormatUtil.parseDateToString(deleteRecordLastSyncTime))
                .flatMap((Function<Response<List<DeleteRecord>>, ObservableSource<DeleteRecord>>) listResponse -> {
                    if (listResponse.isSuccessful()) {
                        Log.i(TAG, "downloadDeleteRecord get right status code");
                        Log.i(TAG, "downloadDeleteRecord: " + listResponse.body());
                        responseDate.setValue(listResponse.headers().getDate("Date"));
                        return Observable.fromIterable(listResponse.body());
                    } else {
                        Log.e(TAG, "downloadDeleteRecord: worng status code:" + listResponse.code());
                        throw errorCollectionService.addWrongStatusCodeError(listResponse.code(), "download DeleteRecord: deleteRecordApi");

                    }
                })
                .doOnNext(record -> {
                    Log.i(TAG, "downloadDeleteRecord:  deal record " + record);
                    if (record.getTableNo() == BOOK_NODE.value()) {
                        List<BookNode> bookNodes = bookNodeRoomService.selectByNoSync(record.getItemNo());
                        if (!bookNodes.isEmpty()) {
                            Log.i(TAG, "downloadDeleteRecord: delete " + bookNodes.get(0) + " form local");
                            bookNodeRoomService.delete(bookNodes.get(0)).subscribe(bookNode1 -> {
                            }, throwable -> {
                                errorCollectionService.addThrowable(throwable, "download DeleteRecord: bookNodeRoomService.delete", "bookNode:" + bookNodes.get(0));
                                Log.e(TAG, "downloadDeleteRecord: ", throwable);
                            });
                        }
                    }

                })
                .doOnComplete(() -> {
                    Log.i(TAG, "downloadDeleteRecord: write " + responseDate.getValue() + " into sharePreference");
                    sharedPreferences.setDeleteRecordLastSyncTime(responseDate.getValue());
                })
                .doOnError(throwable -> {
                    errorCollectionService.addThrowable(throwable, "download deleteRecords:  deleteRecordApi.getAll", null);

                });


    }

    public Observable<DeleteRecord> uploadDeleteRecords() {
        return deleteRecordRoomService.selectAll()
                .toObservable()
                .flatMap((Function<List<DeleteRecord>, ObservableSource<DeleteRecord>>) Observable::fromIterable)
                .doOnNext(record -> {
                    deleteRecordApi.post(record)
                            .doOnComplete(() -> {
                                deleteRecordRoomService.delete(record).subscribe();
                                Log.i(TAG, "uploadDeleteRecords: delete local record" + record);
                            })
                            .subscribe(voidResponse -> {
                                if (voidResponse.isSuccessful()) {
                                    Log.i(TAG, "uploadDeleteRecords: upload record " + record + "  get right status code");
                                } else {
                                    Log.i(TAG, "uploadDeleteRecords: upload record " + record + " get wrong status code " + voidResponse.code());
                                    throw errorCollectionService.addWrongStatusCodeError(voidResponse.code(), "upload deleteRecords: deleteRecordApi.post");
                                }
                            }, throwable -> {

                                errorCollectionService.addThrowable(throwable, "upload deleteRecords: deleteRecordApi.post", "deleteRecord :" + record);
                                Log.e(TAG, "uploadDeleteRecords: ", throwable);
                            });
                })
                .doOnError(throwable -> {
                    errorCollectionService.addThrowable(throwable, "upload deleteRecords: deleteRecordRoomService.selectAll()", null);
                });
    }

    public Single<Object> syncDeleteRecord() {

        return Single.create(emitter -> {
            Semaphore semaphore = new Semaphore(-1, true);
            downloadDeleteRecord().subscribeOn(Schedulers.io())
                    .doFinally(semaphore::release)
                    .subscribe(record -> {
                    }, throwable -> Log.e(TAG, "syncDeleteRecord: ", throwable));

            uploadDeleteRecords().subscribeOn(Schedulers.io())
                    .doFinally(semaphore::release)
                    .subscribe(record -> {
                    }, throwable -> Log.e(TAG, "syncDeleteRecord: ", throwable));
            semaphore.acquire();
            emitter.onSuccess(new Object());
        });


    }

}
