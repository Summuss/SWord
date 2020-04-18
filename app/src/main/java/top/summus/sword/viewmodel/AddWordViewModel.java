package top.summus.sword.viewmodel;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.unnamed.b.atv.model.TreeNode;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import lombok.Setter;
import top.summus.sword.SWordApplication;
import top.summus.sword.component.TextInputTreeNode;
import top.summus.sword.component.WordClassInputTreeNode;
import top.summus.sword.room.entity.BookNode;
import top.summus.sword.room.entity.Meaning;
import top.summus.sword.room.entity.Sentence;
import top.summus.sword.room.entity.Word;
import top.summus.sword.room.service.MeaningRoomService;
import top.summus.sword.room.service.SentenceRoomService;
import top.summus.sword.room.service.WordBookNodeJoinRoomService;
import top.summus.sword.room.service.WordRoomService;

@SuppressLint("CheckResult")

public class AddWordViewModel extends ViewModel {
    private static final String TAG = "AddWordViewModel";

    private AddWordViewModelCallback callback;

    @Setter
    private BookNode bookNode;

    @Inject
    WordRoomService wordRoomService;

    @Inject
    WordBookNodeJoinRoomService wordBookNodeJoinRoomService;

    @Inject
    MeaningRoomService meaningRoomService;

    @Inject
    SentenceRoomService sentenceRoomService;

    public static AddWordViewModel getInstance(@NonNull Fragment fragment) {
        AddWordViewModel addWordViewModel = new ViewModelProvider(fragment).get(AddWordViewModel.class);
        if (fragment instanceof AddWordViewModelCallback) {
            addWordViewModel.callback = (AddWordViewModelCallback) fragment;
        } else {
            throw new UnsupportedOperationException("not implement AddWordModelCallback");
        }
        SWordApplication.getAppComponent().inject(addWordViewModel);
        return addWordViewModel;
    }


    public void add(Word word, TreeNode treeNode) {
        Observable.create(emitter -> {
            wordRoomService.add(word, bookNode.getId())
                    .subscribe((words, throwable) -> {
                        if (throwable != null) {
                        } else {
                            addInformation(treeNode, word.getId());
                        }

                    });
            emitter.onNext(new Object());
            emitter.onComplete();
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    callback.addWordFinished();
                })
                .subscribe(
                        o -> {
                        },
                        throwable -> Log.e(TAG, "add: ", throwable));

    }

    private void addInformation(TreeNode root, long wordId) {

        List<TextInputTreeNode> meaningNodes = new ArrayList<>();
        getMeaningNodes(root, meaningNodes);
        Observable.fromIterable(meaningNodes).subscribe(textInputTreeNode -> {
            WordClassInputTreeNode parent = (WordClassInputTreeNode) textInputTreeNode.getParent();
            int wordClass = parent.getWordClass();
            String meaningString = textInputTreeNode.getInputContent();
            Meaning meaning = Meaning.builder().wordClass(wordClass).meaning(meaningString).wordId(wordId).build();
            meaningRoomService.insert(meaning)
                    .subscribe((aLong, throwable) -> {
                        meaning.setId(aLong);
                    });
            for (TreeNode treeNode : textInputTreeNode.getChildren()) {
                TextInputTreeNode node = (TextInputTreeNode) treeNode;
                String sentenceString = node.getInputContent();
                String interpretation = null;
                if (!node.getChildren().isEmpty()) {
                    interpretation = ((TextInputTreeNode) node.getChildren().get(0)).getInputContent();
                }
                Sentence sentence = Sentence.builder().meaningId(meaning.getId()).sentence(sentenceString).interpretation(interpretation).build();
                sentenceRoomService.insert(sentence).subscribeOn(Schedulers.io()).subscribe();
            }

        });
    }

    private void getMeaningNodes(TreeNode treeNode, List<TextInputTreeNode> meaningNodes) {
        List<TreeNode> children = treeNode.getChildren();
        for (TreeNode child : children) {
            if (child instanceof TextInputTreeNode && ((TextInputTreeNode) child).getType() == TextInputTreeNode.MEANING) {
                meaningNodes.add((TextInputTreeNode) child);
            } else {
                getMeaningNodes(child, meaningNodes);
            }
        }
    }

    public interface AddWordViewModelCallback {

        void addWordFinished();
    }
}
