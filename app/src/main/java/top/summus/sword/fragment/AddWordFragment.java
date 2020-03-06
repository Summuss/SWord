package top.summus.sword.fragment;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.Arrays;
import java.util.List;

import top.summus.sword.R;
import top.summus.sword.activity.AppbarConfigurationSupplier;
import top.summus.sword.databinding.FragmentAddWordBinding;
import top.summus.sword.room.entity.BookNode;
import top.summus.sword.room.entity.Word;
import top.summus.sword.viewmodel.AddWordViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddWordFragment extends Fragment implements AddWordViewModel.AddWordViewModelCallback {
    private static final String TAG = "AddWordFragment";
    private FragmentAddWordBinding binding;

    private AppCompatActivity parentActivity;
    private NavController navController;
    private AddWordViewModel addWordViewModel;

    private BookNode bookNode;

    public AddWordFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getArguments().containsKey("bookNode")) {
            bookNode = (BookNode) getArguments().getSerializable("bookNode");
            addWordViewModel.setBookNode(bookNode);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_word, container, false);
        parentActivity = (AppCompatActivity) getActivity();
        navController = NavHostFragment.findNavController(this);
        addWordViewModel = AddWordViewModel.getInstance(this);
        if (bookNode == null) {
            Log.i(TAG, "onCreateView: bookNode null");
        } else {
            Log.i(TAG, "onCreateView: bookNode not null");
        }
        initTopBar();


        List<String> strings = Arrays.asList(" ⓪", "①", " ②", "③");
        binding.toneSpinner.attachDataSource(strings);

        binding.confirmFbt.setOnClickListener(view -> {
            String content = binding.contentTv.getText().toString();
            String pronun = binding.pronumTv.getText().toString();
            int tone = binding.toneSpinner.getSelectedIndex();
            int priority = (int) binding.priorityRating.getRating();
            int difficulty = binding.diffultyRating.getRating();
            Word word = Word.builder().
                    content(content).pronunciation(pronun).tone(tone).priority(priority).difficulty(difficulty)
                    .build();
            addWordViewModel.add(word);
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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        return true;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.add_word_fragment_menu, menu);
    }

    @Override
    public void addWordFinished() {

    }
}
