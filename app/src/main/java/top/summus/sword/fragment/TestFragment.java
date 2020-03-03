package top.summus.sword.fragment;


import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.os.Trace;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import top.summus.sword.R;
import top.summus.sword.SWordApplication;
import top.summus.sword.adapter.BookNodeRecyclerViewAdapter;
import top.summus.sword.databinding.FragmentTestBinding;
import top.summus.sword.network.service.DeleteRecordHttpService;
import top.summus.sword.room.dao.DeleteRecordDao;
import top.summus.sword.room.entity.DeleteRecord;
import top.summus.sword.room.service.DeleteRecordRoomService;

import static top.summus.sword.room.dao.DeleteRecordDao.Table.BOOK_NODE;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("CheckResult")

public class TestFragment extends Fragment {

    private static final String TAG = "TestFragment";
    private FragmentTestBinding binding;
    int count = 5;
    BookNodeRecyclerViewAdapter adapter;

    @Inject
    DeleteRecordRoomService deleteRecordRoomService;

    @Inject
    DeleteRecordHttpService deleteRecordHttpService;


    public Observable<Integer> func() {

        return Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                Log.i(TAG, "subscribe: "+Thread.currentThread());
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .map(integer -> {
                    Log.i(TAG, "func: in map "+Thread.currentThread());
                    return integer+1;
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_test, container, false);
        SWordApplication.getAppComponent().inject(this);

        deleteRecordHttpService.downloadDeleteRecord().subscribeOn(Schedulers.io())
                .subscribe(record -> {},throwable -> {
                    Log.e(TAG, "onCreateView: ",throwable );
                });

        return binding.getRoot();


    }

}
