package top.summus.sword.fragment;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.animation.OvershootInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.AnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.SlideInRightAnimationAdapter;
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;
import jp.wasabeef.recyclerview.animators.SlideInRightAnimator;
import top.summus.sword.R;
import top.summus.sword.adapter.BookNodeRecyclerViewAdapter;
import top.summus.sword.databinding.FragmentTestBinding;
import top.summus.sword.entity.BookNode;

/**
 * A simple {@link Fragment} subclass.
 */
public class TestFragment extends Fragment implements BookNodeRecyclerViewAdapter.OnBookNodeItemClicked {

    private FragmentTestBinding binding;
    int count = 5;
    BookNodeRecyclerViewAdapter adapter;

    public TestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_test, container, false);


        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        binding.recycelr.setLayoutManager(layoutManager);
        binding.recycelr.setHasFixedSize(true);

        List<BookNode> bookNodes = new ArrayList<>();
        BookNode bookNode = BookNode.builder().nodeName("test").nodeTag(0).build();
        bookNodes.add(bookNode);
        bookNodes.add(bookNode);
        bookNodes.add(bookNode);
        bookNodes.add(bookNode);
        bookNodes.add(bookNode);


        adapter = new BookNodeRecyclerViewAdapter(this, bookNodes);

        ScaleInAnimationAdapter animationAdapter = new ScaleInAnimationAdapter(adapter);
//

        setAnimationAdapter(animationAdapter);


        binding.button.setOnClickListener(v -> {
            BookNode node = BookNode.builder().nodeName("test" + count).build();

            bookNodes.add(node);
            adapter.notifyItemInserted(count++);
            binding.recycelr.scrollToPosition(count - 1);


        });
        binding.button3.setOnClickListener(v -> {
            BookNode bookNode1 = BookNode.builder().nodeName("add" + count++).build();
            bookNodes.add(1, bookNode1);
            adapter.notifyItemInserted(1);
            binding.recycelr.scrollToPosition(1);
        });


        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    private void setAnimationAdapter(AnimationAdapter animationAdapter) {
        animationAdapter.setDuration(1000);
        animationAdapter.setFirstOnly(false);
        animationAdapter.setInterpolator(new OvershootInterpolator());
        binding.recycelr.setAdapter(animationAdapter);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


    @Override
    public void onBookNodeItemClicked(int position, BookNode target) {

    }
}
