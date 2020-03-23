package top.summus.sword.fragment;

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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.NonNull;
import top.summus.sword.R;
import top.summus.sword.activity.AppbarConfigurationSupplier;
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
public class WordFragment extends Fragment implements WordViewModel.WordViewModelCallback, WordRecyclerViewAdapter.WordRecyclerViewAdapterCallback {
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


        LinearLayoutManager layoutManager = new LinearLayoutManager(parentActivity);
        binding.list.setLayoutManager(layoutManager);
        adapter = new WordRecyclerViewAdapter(wordViewModel.getWordsToBeShowed(), this);
        binding.list.setAdapter(adapter);
        parentActivity = (AppCompatActivity) getActivity();

        binding.addWordFbt.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("bookNode", bookNode);
            navController.navigate(R.id.action_wordFragment_to_addWordFragment, bundle);

        });

        return binding.getRoot();
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
    public void onWordItemClick(Word word) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("word", word);
        navController.navigate(R.id.action_wordFragment_to_wordDetailFragment, bundle);
    }
}
