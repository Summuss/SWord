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


import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import top.summus.sword.R;
import top.summus.sword.adapter.BookNodeRecyclerViewAdapter;
import top.summus.sword.databinding.FragmentTestBinding;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("CheckResult")

public class TestFragment extends Fragment {

    private static final String TAG = "TestFragment";
    private FragmentTestBinding binding;
    int count = 5;
    BookNodeRecyclerViewAdapter adapter;

    public TestFragment() {
        // Required empty public constructor
    }

    public Observable<Integer> func(){

        return Observable.just(1,2,3)
                .doOnNext(integer -> {
                    Log.i(TAG, "func: 1");
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_test, container, false);
        Observable<Integer> integerObservable = Observable.just(1).subscribeOn(Schedulers.io())
                .doOnNext(integer -> {
                    Log.i(TAG, "doOnNext1 " + Thread.currentThread().getId());
                }).observeOn(AndroidSchedulers.mainThread());

 Observable.just(1).subscribeOn(Schedulers.io())
                .doOnNext(integer -> {
                    Log.i(TAG, "doOnNext2 "  + Thread.currentThread().getId());
                    integerObservable.subscribe(integer1 -> {
                        Log.i(TAG, "in subscribe "+Thread.currentThread().getId());
                    });
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(integer -> {
                    Log.i(TAG, "onCreateV"+Thread.currentThread().getId());
                });

//        Observable.concat(integerObservable1,integerObservable).subscribeOn(Schedulers.io())
//                .doFinally(() -> Log.i(TAG, "onCreateView: finally"+ Thread.currentThread().getId()))
//                .subscribe(integer -> {
//                    Log.i(TAG, "OnNext3 "  + Thread.currentThread().getId());
//
//                });

        return binding.getRoot();


    }

}
