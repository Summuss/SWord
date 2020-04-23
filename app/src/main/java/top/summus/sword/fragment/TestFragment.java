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
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
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
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import top.summus.sword.R;
import top.summus.sword.SWordApplication;
import top.summus.sword.adapter.BookNodeRecyclerViewAdapter;
import top.summus.sword.adapter.CurrentStudyCardViewAdapter;
import top.summus.sword.component.TextInputTreeNode;
import top.summus.sword.component.WordClassInputTreeNode;
import top.summus.sword.databinding.FragmentTestBinding;
import top.summus.sword.network.service.BookNodeHttpService;
import top.summus.sword.network.service.DeleteRecordHttpService;
import top.summus.sword.room.entity.Word;
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
        parentActivity.setSupportActionBar(binding.toolbar);
        NavController navController = NavHostFragment.findNavController(this);

        AppBarConfiguration appBarConfiguration =
                new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupWithNavController(binding.toolbar, navController);
        binding.toolbar.setTitle("");
        setHasOptionsMenu(true);

        SWordApplication.getAppComponent().inject(this);

        List<Word> words = new ArrayList<>();
        words.add(Word.builder().content("word1").build());
        words.add(Word.builder().content("word2").build());
        words.add(Word.builder().content("word3").build());
        words.add(Word.builder().content("word4").build());
        CardStackLayoutManager cardStackLayoutManager = new CardStackLayoutManager(parentActivity, new CardStackListener() {
            @Override
            public void onCardDragging(Direction direction, float ratio) {
                Log.i(TAG, "onCardDragging: " + direction + "   " + ratio);
            }

            @Override
            public void onCardSwiped(Direction direction) {
                Log.i(TAG, "onCardSwiped: " + direction);

            }

            @Override
            public void onCardRewound() {

            }

            @Override
            public void onCardCanceled() {

            }

            @Override
            public void onCardAppeared(View view, int position) {

            }

            @Override
            public void onCardDisappeared(View view, int position) {

            }
        });
        cardStackLayoutManager.setVisibleCount(3);
        cardStackLayoutManager.setDirections(Direction.FREEDOM);
        binding.cardStackView.setLayoutManager(cardStackLayoutManager);
        CurrentStudyCardViewAdapter currentStudyCardViewAdapter = new CurrentStudyCardViewAdapter(words);
        binding.cardStackView.setAdapter(currentStudyCardViewAdapter);


        return binding.getRoot();


    }


}
