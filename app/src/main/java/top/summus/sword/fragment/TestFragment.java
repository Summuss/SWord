package top.summus.sword.fragment;


import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.util.Property;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.ogaclejapan.smarttablayout.utils.ViewPagerItem;
import com.ogaclejapan.smarttablayout.utils.ViewPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.ViewPagerItems;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import top.summus.sword.R;
import top.summus.sword.SWordApplication;
import top.summus.sword.adapter.BookNodeRecyclerViewAdapter;
import top.summus.sword.component.TextInputTreeNode;
import top.summus.sword.component.WordClassInputTreeNode;
import top.summus.sword.databinding.FragmentTestBinding;
import top.summus.sword.network.service.BookNodeHttpService;
import top.summus.sword.network.service.DeleteRecordHttpService;
import top.summus.sword.room.service.DeleteRecordRoomService;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("CheckResult")

public class TestFragment extends Fragment {

    private static final String TAG = "TestFragment";
    private FragmentTestBinding binding;
    int count = 5;
    BookNodeRecyclerViewAdapter adapter;
    private AppCompatActivity parentActivity;

    @Inject
    DeleteRecordRoomService deleteRecordRoomService;

    @Inject
    DeleteRecordHttpService deleteRecordHttpService;

    @Inject
    BookNodeHttpService bookNodeHttpService;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_test, container, false);
        parentActivity = (AppCompatActivity) getActivity();

        SWordApplication.getAppComponent().inject(this);
//        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
//                parentActivity.getSupportFragmentManager(), FragmentPagerItems.with(getActivity())
//                .add("tab1", WordInfoFragment.class)
//                .add("tab2", WordInfoFragment.class)
//                .create()
//
//        );
        ViewPagerItems viewPagerItems = ViewPagerItems.with(parentActivity).create();
        ViewPagerItemAdapter adapter = new ViewPagerItemAdapter(viewPagerItems);
        viewPagerItems.add(ViewPagerItem.of("hello", R.layout.fragment_word_info));
        viewPagerItems.add(ViewPagerItem.of("world", R.layout.fragment_word_info));
        binding.viewpager.setAdapter(adapter);
        binding.smarttablayout.setViewPager(binding.viewpager);
        Log.i(TAG, "onCreateView: " + binding.viewpager.getCurrentItem());

        binding.smarttablayout.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                Log.i(TAG, "onPageScrolled: "+position+"  "+positionOffset+"  "+positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                Log.i(TAG, "onPageSelected: " + position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.i(TAG, "onPageScrollStateChanged: " + state);

            }
        });

        Log.i(TAG, "onCreateView: ddd");
        Observable.just(1, 2, 3)
                .subscribeOn(Schedulers.io())

                .observeOn(Schedulers.io())
                .doFinally(() -> {
                    System.out.println("in finally " + Thread.currentThread());
                })
                .doAfterNext(integer -> {
                    System.out.println("do on next " + Thread.currentThread());
                })
                .subscribe(integer -> {
                    System.out.println("in subscribe " + integer + "  " + Thread.currentThread());
                });

        return binding.getRoot();


    }


}
