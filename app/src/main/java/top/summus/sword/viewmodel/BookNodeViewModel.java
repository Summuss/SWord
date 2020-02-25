package top.summus.sword.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import top.summus.sword.SWordDatabase;
import top.summus.sword.entity.BookNode;
import top.summus.sword.repository.BookNodeRepository;

import static top.summus.sword.repository.BookNodeRepository.selectByPath;

public class BookNodeViewModel extends ViewModel {


    @Setter
    @Getter
    private MutableLiveData<String> currentPath = new MutableLiveData<>("/");

    @Getter
    private LiveData<List<BookNode>> bookNodesShowed = SWordDatabase.getInstance().getBookNodeDao().getAll();


}
