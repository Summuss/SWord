package top.summus.sword.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;


import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.Direction;

import es.dmoral.toasty.Toasty;
import top.summus.sword.R;
import top.summus.sword.activity.AppbarConfigurationSupplier;
import top.summus.sword.adapter.CurrentStudyCardViewAdapter;
import top.summus.sword.databinding.FragmentCurrentStudyBinding;
import top.summus.sword.room.entity.Word;
import top.summus.sword.viewmodel.CurrentStudyViewModel;


public class CurrentStudyFragment extends Fragment implements CurrentStudyViewModel.CurrentStudyViewModelCallback {

    private static final String TAG = "CurrentStudyFragment";

    private FragmentCurrentStudyBinding binding;
    private AppCompatActivity parentActivity;
    private NavController navController;
    private CurrentStudyViewModel viewModel;
    private CardStackLayoutManager cardStackLayoutManager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_current_study, container, false);
        parentActivity = (AppCompatActivity) getActivity();
        navController = NavHostFragment.findNavController(this);
        viewModel = CurrentStudyViewModel.getInstance(this);


        initTopBar();
        initCardView();
        initListener();


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
        cardStackLayoutManager = new CardStackLayoutManager(parentActivity, new CardStackListener() {
            @Override
            public void onCardDragging(Direction direction, float ratio) {
            }

            @Override
            public void onCardSwiped(Direction direction) {
                Log.i(TAG, "onCardSwiped: " + direction);
//                Word word = viewModel.getWordsNeedToLearn().get(cardStackLayoutManager.getTopPosition());
//                Log.i(TAG, "onCardSwiped: " + word);


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
                Log.i(TAG, "onCardDisappeared: position:" + position);
                if (position == binding.cardStackView.getAdapter().getItemCount() - 1) {
                    Toasty.info(parentActivity, "none", Toasty.LENGTH_SHORT).show();
                }

            }
        });
        cardStackLayoutManager.setVisibleCount(3);
        cardStackLayoutManager.setDirections(Direction.FREEDOM);
        binding.cardStackView.setLayoutManager(cardStackLayoutManager);
        CurrentStudyCardViewAdapter currentStudyCardViewAdapter = new CurrentStudyCardViewAdapter(viewModel.getWordsNeedToLearn());
        currentStudyCardViewAdapter.setCallback(word -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("word", word);
            navController.navigate(R.id.action_CurrentStudyFragment_to_wordDetailFragment, bundle);
        });
        binding.cardStackView.setAdapter(currentStudyCardViewAdapter);
    }

    private void initListener() {
        binding.knowBt.setOnClickListener(view -> {
            Word word = viewModel.getWordsNeedToLearn().get(cardStackLayoutManager.getTopPosition());
            word.know();
            viewModel.updateWord(word);
            binding.cardStackView.swipe();
        });
        binding.ambiguousBt.setOnClickListener(view -> {
            Word word = viewModel.getWordsNeedToLearn().get(cardStackLayoutManager.getTopPosition());
            word.ambiguous();
            viewModel.updateWord(word);
            binding.cardStackView.swipe();
        });
        binding.forgetBt.setOnClickListener(view -> {
            Word word = viewModel.getWordsNeedToLearn().get(cardStackLayoutManager.getTopPosition());
            word.forget();
            viewModel.updateWord(word);
            binding.cardStackView.swipe();
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.base_word_list_fragment_menu, menu);
    }


}
