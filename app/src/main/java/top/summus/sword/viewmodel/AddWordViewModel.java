package top.summus.sword.viewmodel;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import javax.inject.Inject;

import io.reactivex.schedulers.Schedulers;
import lombok.Setter;
import top.summus.sword.SWordApplication;
import top.summus.sword.room.entity.BookNode;
import top.summus.sword.room.entity.Word;
import top.summus.sword.room.entity.WordBookNodeJoin;
import top.summus.sword.room.service.WordBookNodeJoinRoomService;
import top.summus.sword.room.service.WordRoomService;

@SuppressLint("CheckResult")

public class AddWordViewModel extends ViewModel {

    private AddWordViewModelCallback callback;

    @Setter
    private BookNode bookNode;

    @Inject
    WordRoomService wordRoomService;

    @Inject
    WordBookNodeJoinRoomService wordBookNodeJoinRoomService;

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


    public void add(Word word) {
        wordRoomService.add(word, bookNode.getId()).subscribeOn(Schedulers.io())
                .doFinally(() -> {
                    callback.addWordFinished();
                })
                .subscribe((word1, throwable) -> {
                    if (throwable != null) {

                    }
                });
    }

    public interface AddWordViewModelCallback {

        void addWordFinished();
    }
}
