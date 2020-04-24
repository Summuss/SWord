package top.summus.sword.fragment;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.matrixxun.starry.badgetextview.MaterialBadgeTextView;
import com.ogaclejapan.smarttablayout.utils.ViewPagerItem;
import com.ogaclejapan.smarttablayout.utils.ViewPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.ViewPagerItems;

import top.summus.sword.R;
import top.summus.sword.activity.AppbarConfigurationSupplier;
import top.summus.sword.databinding.FragmentWordDetailBinding;
import top.summus.sword.room.entity.Meaning;
import top.summus.sword.room.entity.Sentence;
import top.summus.sword.room.entity.Word;
import top.summus.sword.viewmodel.WordDetailViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class WordDetailFragment extends Fragment implements WordDetailViewModel.WordDetailViewModelCallback {
    private static final String TAG = "WordDetailFragment";
    private FragmentWordDetailBinding binding;
    private AppCompatActivity parentActivity;
    private NavController navController;
    private WordDetailViewModel wordDetailViewModel;
    ViewPagerItems viewPagerItems;
    ViewPagerItemAdapter viewPagerItemAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_word_detail, container, false);
        navController = NavHostFragment.findNavController(this);
        parentActivity = (AppCompatActivity) getActivity();
        wordDetailViewModel = WordDetailViewModel.getInstance(this);
        initTopBar();
        initListened();

        binding.priorityRating.setClickable(false);


        binding.smarttablayout.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.i(TAG, "onPageSelected: " + position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getArguments().containsKey("word")) {
            wordDetailViewModel.setWord((Word) getArguments().get("word"));
            binding.setViewModel(wordDetailViewModel);
            binding.priorityRating.setRating(wordDetailViewModel.getWord().getPriority());
            binding.toneTv.setBadgeCount(wordDetailViewModel.getWord().getTone(), false);
        }
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

    private void initListened() {
        binding.editFbt.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("wordClassMeaningsMap", wordDetailViewModel.getWordClassMeaningsMap());
            bundle.putSerializable("meaningSentenceMap", wordDetailViewModel.getMeaningSentenceMap());
            navController.navigate(R.id.action_wordDetailFragment_to_editWordFragment, bundle);
            ;
        });
    }

    private void loadWordInfoView(Meaning.WordClass wordClass, int position) {

        for (Meaning meaning : wordDetailViewModel.getWordClassMeaningsMap().get(wordClass)) {

            View view = viewPagerItemAdapter.getPage(position).findViewById(R.id.word_info_outer);
            View meaningView = getLayoutInflater().inflate(R.layout.word_detail_meaning_display_layout, (ViewGroup) view, false);
            MaterialBadgeTextView meaningTV = meaningView.findViewById(R.id.meaning_display);
            meaningTV.setText(meaning.getMeaning());
            ((ViewGroup) view).addView(meaningView);
            for (Sentence sentence : wordDetailViewModel.getMeaningSentenceMap().get(meaning.getId())) {
                View sentenceView = getLayoutInflater().inflate(R.layout.word_detail_sentence_layout, (ViewGroup) view, false);
                TextView sentenceTv = sentenceView.findViewById(R.id.word_detail_sentence_tv);
                TextView interpretationTv = sentenceView.findViewById(R.id.word_detail_interpretation_tv);
                sentenceTv.setText(sentence.getSentence());
                if (sentence.getInterpretation() != null) {
                    interpretationTv.setVisibility(View.VISIBLE);
                    interpretationTv.setText(sentence.getInterpretation());
                }
                ((ViewGroup) view).addView(sentenceView);

            }

        }

    }


    @Override
    public void onLoadWordInfoFinished() {
        viewPagerItems = ViewPagerItems.with(parentActivity).create();
        viewPagerItemAdapter = new ViewPagerItemAdapter(viewPagerItems);
        Log.i(TAG, "onLoadWordInfoFinished: " + wordDetailViewModel.getWordClassMeaningsMap().keySet().size());
        int i = 0;
        for (Meaning.WordClass wordClass : wordDetailViewModel.getWordClassMeaningsMap().keySet()) {
            ViewPagerItem item = ViewPagerItem.of(wordClass.getWordClass(), R.layout.fragment_word_info);
            viewPagerItems.add(item);
        }
//        viewPagerItems.add(ViewPagerItem.of("hello",R.layout.fragment_word_info));
//        viewPagerItems.add(ViewPagerItem.of("world",R.layout.fragment_word_info));
//
        binding.viewpager.setAdapter(viewPagerItemAdapter);
        binding.smarttablayout.setViewPager(binding.viewpager);
        if (viewPagerItemAdapter.getPage(0) == null) {
            Log.i(TAG, "onLoadWordInfoFinished: null");
        } else {
            Log.i(TAG, "onLoadWordInfoFinished: not null");
        }
        for (Meaning.WordClass wordClass : wordDetailViewModel.getWordClassMeaningsMap().keySet()) {
            loadWordInfoView(wordClass, i++);
        }

    }
}
