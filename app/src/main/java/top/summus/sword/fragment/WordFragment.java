package top.summus.sword.fragment;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import top.summus.sword.R;
import top.summus.sword.adapter.WordRecyclerViewAdapter;
import top.summus.sword.databinding.FragmentWordBinding;
import top.summus.sword.room.entity.Word;


/**
 * A fragment representing a list of Items.
 * <p/>
 * interface.
 */
public class WordFragment extends Fragment {

    private FragmentWordBinding binding;
    private AppCompatActivity parentActivity;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_word, container, false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(parentActivity);
        binding.list.setLayoutManager(layoutManager);
        List<Word> words = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            words.add(new Word());
        }
        WordRecyclerViewAdapter adapter = new WordRecyclerViewAdapter(words);
        binding.list.setAdapter(adapter);


        return binding.getRoot();
    }


}
