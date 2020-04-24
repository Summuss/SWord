package top.summus.sword.fragment;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import top.summus.sword.component.WordClassInputTreeNode;
import top.summus.sword.room.entity.BookNode;
import top.summus.sword.room.entity.Meaning;
import top.summus.sword.room.entity.Sentence;

public class EditWordFragment extends AddWordFragment {
    private static final String TAG = "EditWordFragment";
    private Map<Meaning.WordClass, List<Meaning>> wordClassMeaningsMap;
    private Map<Long, List<Sentence>> meaningSentenceMap;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getArguments().containsKey("wordClassMeaningsMap")) {
            wordClassMeaningsMap = (Map<Meaning.WordClass, List<Meaning>>) getArguments().getSerializable("wordClassMeaningsMap");
        }
        if (getArguments().containsKey("meaningSentenceMap")) {
            meaningSentenceMap = (Map<Long, List<Sentence>>) getArguments().getSerializable("meaningSentenceMap");
        }
        loadInfo();
    }

    @Override
    protected void subInit() {
//        loadInfo();
    }

    private void loadInfo() {
        for (Meaning.WordClass wordClass : wordClassMeaningsMap.keySet()) {
            Log.i(TAG, "loadInfo: " + wordClass.value());
            WordClassInputTreeNode wordClassInputTreeNode = new WordClassInputTreeNode(getContext(), treeView, wordClass.value());


        }
    }
}
