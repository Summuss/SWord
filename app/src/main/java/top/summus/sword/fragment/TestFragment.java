package top.summus.sword.fragment;


import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import top.summus.sword.R;
import top.summus.sword.SWordApplication;
import top.summus.sword.adapter.BookNodeRecyclerViewAdapter;
import top.summus.sword.component.TextInputTreeNode;
import top.summus.sword.component.WordClassInputTreeNode;
import top.summus.sword.databinding.FragmentTestBinding;
import top.summus.sword.network.service.BookNodeHttpService;
import top.summus.sword.network.service.DeleteRecordHttpService;
import top.summus.sword.room.service.DeleteRecordRoomService;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("CheckResult")

public class TestFragment extends Fragment {

    private static final String TAG = "TestFragment";
    private FragmentTestBinding binding;
    int count = 5;
    BookNodeRecyclerViewAdapter adapter;
    private AppCompatActivity parentActivity;

    @Inject
    DeleteRecordRoomService deleteRecordRoomService;

    @Inject
    DeleteRecordHttpService deleteRecordHttpService;

    @Inject
    BookNodeHttpService bookNodeHttpService;


    public Observable<Integer> func() {

        return Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                Log.i(TAG, "subscribe: " + Thread.currentThread());
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .map(integer -> {
                    Log.i(TAG, "func: in map " + Thread.currentThread());
                    return integer + 1;
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_test, container, false);

        SWordApplication.getAppComponent().inject(this);

        TreeNode root = TreeNode.root();
        AndroidTreeView treeView = new AndroidTreeView(getContext(), root);

//        WordClassInputTreeNode wordClassInput = new WordClassInputTreeNode(treeView);
        TextInputTreeNode meaningInputTreeNode = new TextInputTreeNode(TextInputTreeNode.MEANING, treeView, getContext());
        TextInputTreeNode sentenceInputTreeNode = new TextInputTreeNode(TextInputTreeNode.SENTENCE, treeView, getContext());
        TextInputTreeNode interpretationInputTreeNode = new TextInputTreeNode(TextInputTreeNode.SENTENCE_INTERPRETATION, treeView, getContext());

//        TreeNode wordClassNode = new TreeNode(wordClassInput).setViewHolder(wordClassInput.new ViewHolder(getContext()));
        WordClassInputTreeNode wordClassInputTreeNode = new WordClassInputTreeNode(getContext(), treeView);

//
//        sentenceInputTreeNode.addChild(interpretationInputTreeNode);
//
//        meaningInputTreeNode.addChild(sentenceInputTreeNode);
////
//        wordClassInputTreeNode.addChild(meaningInputTreeNode);
        root.addChild(wordClassInputTreeNode);
        treeView.setDefaultNodeClickListener(null);


        binding.treeViewContainer.addView(treeView.getView());


        return binding.getRoot();


    }


}
