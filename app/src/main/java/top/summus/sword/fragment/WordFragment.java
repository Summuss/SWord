package top.summus.sword.fragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;

import com.yanzhenjie.recyclerview.OnItemMenuClickListener;
import com.yanzhenjie.recyclerview.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.SwipeMenuItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import lombok.NonNull;
import top.summus.sword.R;
import top.summus.sword.activity.AppbarConfigurationSupplier;
import top.summus.sword.adapter.BookNodeRecyclerViewAdapter;
import top.summus.sword.adapter.WordRecyclerViewAdapter;
import top.summus.sword.databinding.FragmentWordBinding;
import top.summus.sword.room.entity.BookNode;
import top.summus.sword.room.entity.Word;
import top.summus.sword.viewmodel.WordViewModel;


/**
 * A fragment representing a list of Items.
 * <p/>
 * interface.
 */
public class WordFragment extends Fragment implements WordViewModel.WordViewModelCallback, WordRecyclerViewAdapter.WordRecyclerViewAdapterCallback, OnItemMenuClickListener {
    private static final String TAG = "WordFragment";

    private FragmentWordBinding binding;
    private AppCompatActivity parentActivity;
    private NavController navController;
    private BookNode bookNode;
    private WordViewModel wordViewModel;
    private WordRecyclerViewAdapter adapter;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getArguments().containsKey("bookNode")) {
            Log.i(TAG, "onActivityCreated: bookNode is passed");
            bookNode = (BookNode) getArguments().getSerializable("bookNode");
            wordViewModel.setWordBook(bookNode);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_word, container, false);
        navController = NavHostFragment.findNavController(this);
        parentActivity = (AppCompatActivity) getActivity();
        wordViewModel = WordViewModel.getInstance(this);
        initTopBar();
        initRecyclerView();


//        LinearLayoutManager layoutManager = new LinearLayoutManager(parentActivity);
//        binding.list.setLayoutManager(layoutManager);
//        adapter = new WordRecyclerViewAdapter(wordViewModel.getWordsToBeShowed(), this);
//        binding.list.setAdapter(adapter);
        parentActivity = (AppCompatActivity) getActivity();

        binding.addWordFbt.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("bookNode", bookNode);
            navController.navigate(R.id.action_wordFragment_to_addWordFragment, bundle);

        });

        return binding.getRoot();
    }

    private void initSwipeMenu() {
        //set swipe menu
        SwipeMenuCreator swipeMenuCreator =
                (leftMenu, rightMenu, position) -> {

                    SwipeMenuItem studyItem =
                            new SwipeMenuItem(parentActivity)
                                    .setHeight(android.app.ActionBar.LayoutParams.MATCH_PARENT)
                                    .setWidth(150)
                                    .setText("study")
                                    .setBackgroundColor(Color.parseColor("#23Df8B"));
                    rightMenu.addMenuItem(studyItem);

                    SwipeMenuItem editItem =
                            new SwipeMenuItem(parentActivity)
                                    .setHeight(android.app.ActionBar.LayoutParams.MATCH_PARENT)
                                    .setWidth(150)
                                    .setText("edit")
                                    .setBackgroundColor(Color.parseColor("#23Df8B"));
                    rightMenu.addMenuItem(editItem);

                    SwipeMenuItem deleteItem =
                            new SwipeMenuItem(parentActivity)
                                    .setHeight(android.app.ActionBar.LayoutParams.MATCH_PARENT)
                                    .setWidth(150)
                                    .setBackgroundColor(Color.parseColor("#FF0000"))
                                    .setText("delete");

                    rightMenu.addMenuItem(deleteItem);
                };
        binding.wordListRecycler.setSwipeMenuCreator(swipeMenuCreator);
        binding.wordListRecycler.setOnItemMenuClickListener(this);

    }

    private void initAdapter() {
        //set adapter
        adapter = new WordRecyclerViewAdapter(wordViewModel.getWordsToBeShowed(), this);
        ScaleInAnimationAdapter animationAdapter = new ScaleInAnimationAdapter(adapter);
        animationAdapter.setDuration(1000);
        animationAdapter.setFirstOnly(false);
        animationAdapter.setInterpolator(new OvershootInterpolator());
        binding.wordListRecycler.setAdapter(animationAdapter);
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(parentActivity);
        binding.wordListRecycler.setLayoutManager(layoutManager);
        initSwipeMenu();
        initAdapter();
        binding.wordListRecycler.setHasFixedSize(true);
    }

    private void initTopBar() {
        parentActivity.setSupportActionBar(binding.toolbar);


        NavigationUI.setupActionBarWithNavController(parentActivity, navController);
        binding.toolbar.setTitle("");
        setHasOptionsMenu(true);
        if (parentActivity instanceof AppbarConfigurationSupplier) {
            NavigationUI.setupWithNavController(binding.collapsingToolbar,
                    binding.toolbar, navController,
                    ((AppbarConfigurationSupplier) parentActivity).getAppBarConfiguration());

        } else {
            throw new RuntimeException("parentActivity not implement AppbarConfigurationSupplier");
        }

    }

    @Override
    public void onLoadWordsFinished() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void addToStudyFinished(boolean successful) {

    }

    @Override
    public void onWordItemClick(Word word) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("word", word);
        navController.navigate(R.id.action_wordFragment_to_wordDetailFragment, bundle);
    }

    //swipeMenu click
    @Override
    public void onItemClick(SwipeMenuBridge swipeMenuBridge, int i) {
        // 左侧还是右侧菜单：
        int direction = swipeMenuBridge.getDirection();
        // 菜单在Item中的Position：
        int menuPosition = swipeMenuBridge.getPosition();
        Word word = adapter.getMValues().get(i);
        if (direction == -1) {
            if (menuPosition == 0) {
                Log.i(TAG, "onItemClick: add to study");
            }
        }
        binding.wordListRecycler.smoothCloseMenu();

    }
}
