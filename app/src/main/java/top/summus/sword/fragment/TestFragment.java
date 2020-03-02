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
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import top.summus.sword.R;
import top.summus.sword.SWordApplication;
import top.summus.sword.adapter.BookNodeRecyclerViewAdapter;
import top.summus.sword.databinding.FragmentTestBinding;
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


    public Observable<Integer> func() {

        return Observable.just(1, 2, 3)
                .doOnNext(integer -> {
                    Log.i(TAG, "func: 1");
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_test, container, false);
        SWordApplication.getAppComponent().inject(this);

        Observable.just(1, 2, 3).observeOn(Schedulers.io())
                .doOnNext(integer -> {
                    Observable.just(1).subscribe(integer1 -> {
                        throw  new RuntimeException();
                    });
                })
                .doFinally(() -> {
//                    throw new RuntimeException();

                })


                .subscribe(integer -> {
                }, throwable -> {
                    Log.i(TAG, "onCreateView: sdfsdfsdfdsfsdfs");
                    Log.e(TAG, "onCreateView: ", throwable);
                });


        return binding.getRoot();


    }

}
