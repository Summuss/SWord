package top.summus.sword.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;


import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.Direction;

import java.util.ArrayList;
import java.util.List;

import top.summus.sword.R;
import top.summus.sword.activity.AppbarConfigurationSupplier;
import top.summus.sword.adapter.CurrentStudyCardViewAdapter;
import top.summus.sword.databinding.FragmentCurrentStudyBinding;
import top.summus.sword.room.entity.Word;


public class CurrentStudyFragment extends Fragment {

    private static final String TAG = "CurrentStudyFragment";

    private FragmentCurrentStudyBinding binding;
    private AppCompatActivity parentActivity;
    private NavController navController;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_current_study, container, false);
        parentActivity = (AppCompatActivity) getActivity();
        navController = NavHostFragment.findNavController(this);

        initTopBar();
        initCardView();


        return binding.getRoot();
    }

    private void initTopBar() {
        NavController navController = NavHostFragment.findNavController(this);
        parentActivity.setSupportActionBar(binding.toolbar);

        NavigationUI.setupActionBarWithNavController(parentActivity, navController);
        AppBarConfiguration appBarConfiguration =
                new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupWithNavController(binding.toolbar, navController, ((AppbarConfigurationSupplier) parentActivity).getAppBarConfiguration());
        binding.toolbar.setTitle("");
        setHasOptionsMenu(true);

    }


    private void initCardView() {
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
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.base_word_list_fragment_menu, menu);
    }


}
