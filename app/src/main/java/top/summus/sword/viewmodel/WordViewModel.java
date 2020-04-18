package top.summus.sword.viewmodel;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.Insert;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import top.summus.sword.SWordApplication;
import top.summus.sword.room.entity.BookNode;
import top.summus.sword.room.entity.CurrentStudyWord;
import top.summus.sword.room.entity.Word;
import top.summus.sword.room.service.CurrentStudyWordRoomService;
import top.summus.sword.room.service.DeleteRecordRoomService;
import top.summus.sword.room.service.WordBookNodeJoinRoomService;
import top.summus.sword.room.service.WordRoomService;

@SuppressLint("CheckResult")

public class WordViewModel extends ViewModel {
    private static final String TAG = "WordViewModel";
    @Getter
    private List<Word> wordsToBeShowed = new ArrayList<>();

    private BookNode wordBook;

    private WordViewModelCallback callback;

    @Inject
    WordBookNodeJoinRoomService wordBookNodeJoinRoomService;

    @Inject
    WordRoomService wordRoomService;

    @Inject
    DeleteRecordRoomService deleteRecordRoomService;

    @Inject
    CurrentStudyWordRoomService currentStudyWordRoomService;

    public static WordViewModel getInstance(@NonNull Fragment fragment) {
        WordViewModel wordViewModel = new ViewModelProvider(fragment).get(WordViewModel.class);
        if (fragment instanceof WordViewModelCallback) {
            wordViewModel.callback = (WordViewModelCallback) fragment;
        } else {
            throw new UnsupportedOperationException("without implementing WordViewModelCallback");
        }
        SWordApplication.getAppComponent().inject(wordViewModel);
        return wordViewModel;
    }

    public void setWordBook(@NonNull BookNode book) {
        wordBookNodeJoinRoomService.selectWordsByBookNodeId(book.getId())
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe((words, throwable) -> {
                    if (words != null) {
                        wordsToBeShowed.clear();
                        wordsToBeShowed.addAll(words);
                        callback.onLoadWordsFinished();
                    }
                    if (throwable != null) {
                        Log.e(TAG, "setWordBook", throwable);
                    }
                });
        wordsToBeShowed.clear();
    }

    public void addToStudy(@NonNull Word word) {
        CurrentStudyWord currentStudyWord = CurrentStudyWord.builder().wordId(word.getId()).build();
        currentStudyWordRoomService.insert(currentStudyWord)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess(aLong -> {
                    callback.addToStudyFinished(true);
                })
                .subscribe((aLong, throwable) -> {
                    if (throwable != null) {
                        Log.e(TAG, "addToStudy: ", throwable);
                    }
                });
    }

    public interface WordViewModelCallback {

        void onLoadWordsFinished();

        void addToStudyFinished(boolean successful);
    }

}
