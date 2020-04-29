package top.summus.sword.fragment;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import com.unnamed.b.atv.model.TreeNode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import top.summus.sword.component.TextInputTreeNode;
import top.summus.sword.component.WordClassInputTreeNode;
import top.summus.sword.room.entity.BookNode;
import top.summus.sword.room.entity.Meaning;
import top.summus.sword.room.entity.Sentence;
import top.summus.sword.room.entity.Word;

public class EditWordFragment extends AddWordFragment {
    private static final String TAG = "EditWordFragment";
    private Map<Meaning.WordClass, List<Meaning>> wordClassMeaningsMap;
    private Map<Long, List<Sentence>> meaningSentenceMap;
    private Word word;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getArguments().containsKey("wordClassMeaningsMap")) {
            wordClassMeaningsMap = (Map<Meaning.WordClass, List<Meaning>>) getArguments().getSerializable("wordClassMeaningsMap");
        }
        if (getArguments().containsKey("meaningSentenceMap")) {
            meaningSentenceMap = (Map<Long, List<Sentence>>) getArguments().getSerializable("meaningSentenceMap");
        }
        if (getArguments().containsKey("word")) {
            word = (Word) getArguments().getSerializable("word");
        }
        loadWordHeaderInfo();
        loadWordClass();
    }


    private void loadWordHeaderInfo() {
        binding.contentTv.setText(word.getContent());
        binding.pronumTv.setText(word.getPronunciation());
        binding.toneSpinner.setSelectedIndex(word.getTone());
        binding.priorityRating.setRating(word.getPriority());
        binding.diffultyRating.setRating(word.getDifficulty());
    }

    @Override
    protected void subInit() {
//        loadInfo();

    }

    private void loadWordClass() {
        for (Meaning.WordClass wordClass : wordClassMeaningsMap.keySet()) {
            Log.i(TAG, "loadInfo: " + wordClass.value());
            WordClassInputTreeNode wordClassInputTreeNode = new WordClassInputTreeNode(getContext(), treeView, wordClass.value());
            loadMeaningAndSentence(wordClass, wordClassInputTreeNode);
            rootNode.addChild(wordClassInputTreeNode);
            treeView.expandNode(rootNode);
        }
    }

    private void loadMeaningAndSentence(Meaning.WordClass wordClass, WordClassInputTreeNode wordClassNode) {
        List<Meaning> meaningList = wordClassMeaningsMap.get(wordClass);
        for (Meaning meaning : meaningList) {
            TextInputTreeNode meaningNode = wordClassNode.createAndInsertMeaning();
            meaningNode.setText(meaning.getMeaning());
            List<Sentence> sentenceList = meaningSentenceMap.get(meaning.getId());
            for (Sentence sentence : sentenceList) {
                TextInputTreeNode sentenceNode = meaningNode.creatAndInsert();
                sentenceNode.setText(sentence.getSentence());
                if (sentence.getInterpretation() != null) {
                    TextInputTreeNode interpretationNode = sentenceNode.creatAndInsert();
                    interpretationNode.setText(sentence.getInterpretation());
                }
            }
        }
    }

    @Override
    protected void confirm() {
        String content = binding.contentTv.getText().toString();
        String pronun = binding.pronumTv.getText().toString();
        int tone = binding.toneSpinner.getSelectedIndex();
        int priority = (int) binding.priorityRating.getRating();
        int difficulty = binding.diffultyRating.getRating();
        Word newWord = Word.builder()
                .id(word.getId())
                .content(content).pronunciation(pronun).tone(tone).priority(priority).difficulty(difficulty)
                .build();
        addWordViewModel.updateWord(newWord, rootNode);
    }

    @Override
    public void onUpdateFinished() {
        Toasty.info(getContext(), "update successfully", Toasty.LENGTH_SHORT).show();
    }
}
