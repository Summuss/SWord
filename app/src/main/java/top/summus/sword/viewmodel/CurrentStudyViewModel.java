package top.summus.sword.viewmodel;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import lombok.Getter;
import lombok.NonNull;
import top.summus.sword.SWordApplication;
import top.summus.sword.exception.MethodNotImplementedException;
import top.summus.sword.room.entity.CurrentStudyWord;
import top.summus.sword.room.entity.Word;
import top.summus.sword.room.service.CurrentStudyWordRoomService;
import top.summus.sword.room.service.WordRoomService;

@SuppressLint("CheckResult")

public class CurrentStudyViewModel extends ViewModel {
    private static final String TAG = "CurrentStudyViewModel";
    private CurrentStudyViewModelCallback callback;

    @Getter
    private List<Word> wordsNeedToLearn = new ArrayList<>();

    @Inject
    CurrentStudyWordRoomService currentStudyWordRoomService;

    @Inject
    WordRoomService wordRoomService;

    public static CurrentStudyViewModel getInstance(@NonNull Fragment fragment) {
        CurrentStudyViewModel viewModel = new ViewModelProvider(fragment).get(CurrentStudyViewModel.class);
        viewModel.inject();
        viewModel.initWordsNeedToLean();
        if (fragment instanceof CurrentStudyViewModelCallback) {
            viewModel.callback = (CurrentStudyViewModelCallback) fragment;
        } else {
            throw new MethodNotImplementedException();
        }
        return viewModel;
    }

    private void inject() {
        SWordApplication.getAppComponent().inject(this);
    }

    private void initWordsNeedToLean() {
        currentStudyWordRoomService.selectToBeLeaned().subscribeOn(Schedulers.io())
                .subscribe((words, throwable) -> {
                    if (words != null) {
                        wordsNeedToLearn.clear();
                        wordsNeedToLearn.addAll(words);
                    }
                    if (throwable != null) {
                        Log.e(TAG, "initWordsNeedToLean: ", throwable);
                    }
                });
    }

    public void updateWord(Word word) {
        wordRoomService.update(word).subscribeOn(Schedulers.io())
                .subscribe();
        //todo
        currentStudyWordRoomService.deleteByWordId(word.getId())
                .subscribeOn(Schedulers.io())
                .subscribe();
    }


    public interface CurrentStudyViewModelCallback {

    }
}
