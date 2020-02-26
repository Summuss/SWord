package top.summus.sword.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import top.summus.sword.SWordDatabase;
import top.summus.sword.dao.BookNodeDao;
import top.summus.sword.entity.BookNode;
import top.summus.sword.repository.BookNodeRepository;

import static top.summus.sword.repository.BookNodeRepository.selectByPath;

public class BookNodeViewModel extends ViewModel {


    private BookNodeDao bookNodeDao = SWordDatabase.getInstance().getBookNodeDao();

    @Getter
    private MutableLiveData<String> currentPath = new MutableLiveData<>("/");

    @Getter
    private LiveData<List<BookNode>> bookNodesShowed = bookNodeDao.selectByPath(currentPath.getValue());

    public void insert(BookNode bookNode) {
        BookNodeRepository.insert(bookNode);
    }

    public void updateShowed() {
        bookNodesShowed = null;
        Log.i("BookNodeFragment", "updateShowed: " + Thread.currentThread().getName());
        bookNodesShowed = bookNodeDao.selectByPath(currentPath.getValue());

    }


}
