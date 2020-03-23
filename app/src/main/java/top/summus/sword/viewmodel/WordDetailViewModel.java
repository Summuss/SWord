package top.summus.sword.viewmodel;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import lombok.Getter;
import lombok.Setter;
import top.summus.sword.SWordApplication;
import top.summus.sword.exception.MethodNotImplementedException;
import top.summus.sword.room.entity.Meaning;
import top.summus.sword.room.entity.Sentence;
import top.summus.sword.room.entity.Word;
import top.summus.sword.room.service.MeaningRoomService;
import top.summus.sword.room.service.SentenceRoomService;

import static io.reactivex.android.schedulers.AndroidSchedulers.*;
import static io.reactivex.schedulers.Schedulers.*;
import static top.summus.sword.room.entity.Meaning.WordClass.getEnum;

@SuppressLint("CheckResult")
public class WordDetailViewModel extends ViewModel {
    private static final String TAG = "WordDetailViewModel";

    private WordDetailViewModelCallback callback;
    @Getter
    private List<Meaning> meanings = new ArrayList<>();

    @Getter
    private Map<Meaning.WordClass, List<Meaning>> wordClassMeaningsMap = new HashMap<>();

    @Getter
    private Map<Long, List<Sentence>> meaningSentenceMap = new HashMap<>();
    @Getter
    private Word word;

    @Inject
    MeaningRoomService meaningRoomService;
    @Inject
    SentenceRoomService sentenceRoomService;

    public static WordDetailViewModel getInstance(@NonNull Fragment fragment) {
        WordDetailViewModel wordDetailViewModel = new ViewModelProvider(fragment).get(WordDetailViewModel.class);
        if (fragment instanceof WordDetailViewModelCallback) {
            wordDetailViewModel.callback = (WordDetailViewModelCallback) fragment;
        } else {
            throw new MethodNotImplementedException();
        }
        wordDetailViewModel.inject();

        return wordDetailViewModel;
    }

    public void setWord(Word word) {
        this.word = word;
        loadWordInfo();
    }

    private void loadWordInfo() {

        meaningRoomService.selectByWordId(word.getId()).subscribeOn(io())
                .doOnSuccess(meanings1 -> {
                    Log.i(TAG, "loadWordInfo: do on success");
                    meanings.addAll(meanings1);
                })
                .toObservable()
                .flatMap((Function<List<Meaning>, ObservableSource<Meaning>>) Observable::fromIterable)
                .observeOn(mainThread())
                .doFinally(() -> {
                    Log.i(TAG, "loadWordInfo: in finally " + Thread.currentThread());
                    callback.onLoadWordInfoFinished();
                })
                .subscribe(meaning -> {
                    Log.i(TAG, "loadWordInfo: in subscribe  " + Thread.currentThread());
                    Log.i(TAG, "loadWordInfo: " + meaning);
                    Meaning.WordClass wordClass = getEnum(meaning.getWordClass());
                    if (!wordClassMeaningsMap.containsKey(wordClass)) {
                        Log.i(TAG, "loadWordInfo: not");
                        List<Meaning> meaningList = new ArrayList<>();
                        meaningList.add(meaning);
                        wordClassMeaningsMap.put(wordClass, meaningList);
                        Log.i(TAG, "loadWordInfo: " + wordClassMeaningsMap.keySet().size());

                    } else {
                        Log.i(TAG, "loadWordInfo: exist");
                        wordClassMeaningsMap.get(wordClass).add(meaning);
                    }
                    sentenceRoomService.selectByMeaningId(meaning.getId())
                            .subscribe((sentences1, throwable) -> {
                                meaningSentenceMap.put(meaning.getId(), sentences1);
                            });
                });
    }

    private void inject() {
        SWordApplication.getAppComponent().inject(this);
    }

    public interface WordDetailViewModelCallback {

        void onLoadWordInfoFinished();
    }
}
