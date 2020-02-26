package top.summus.sword.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.yanzhenjie.recyclerview.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.SwipeMenuItem;

import java.util.ArrayList;
import java.util.Date;


import top.summus.sword.R;
import top.summus.sword.activity.AppbarConfigurationSupplier;
import top.summus.sword.adapter.BookNodeRecyclerViewAdapter;
import top.summus.sword.component.InputPopupWindow;
import top.summus.sword.databinding.FragmentBooknodeBinding;
import top.summus.sword.entity.BookNode;
import top.summus.sword.viewmodel.BookNodeViewModel;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class BookNodeFragment extends Fragment implements BookNodeRecyclerViewAdapter.OnBookNodeItemClicked {

    private static final String TAG = "BookNodeFragment";
    private OnListFragmentInteractionListener mListener;
    private FragmentBooknodeBinding binding;
    private AppCompatActivity parentActivity;
    private NavController navController;
    private BookNodeViewModel bookNodeViewModel;
    BookNodeRecyclerViewAdapter adapter;


    public static BookNodeFragment newInstance(int columnCount) {
        BookNodeFragment fragment = new BookNodeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            //todo
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_booknode, container, false);
        parentActivity = (AppCompatActivity) getActivity();
        navController = NavHostFragment.findNavController(this);
        initRecyclerView();
        initTopBar();

        bookNodeViewModel = ViewModelProviders.of(parentActivity).get(BookNodeViewModel.class);

        bookNodeViewModel.getCurrentPath().observe(parentActivity, s -> {
            Log.i(TAG, " switch path to:" + s);
            if ("/".equals(s)) {
                binding.backFolder.setVisibility(View.GONE);
            } else {
                binding.backFolder.setVisibility(View.VISIBLE);
            }

            //cancel last liveData's observer,because liveData's reference has changed.
            bookNodeViewModel.getBookNodesShowed().removeObservers(parentActivity);
            bookNodeViewModel.updateShowed();
            bookNodeViewModel.getBookNodesShowed().observe(parentActivity, bookNodes -> {
                Log.i(TAG, "begin observe path :" + s);
                adapter.getBookNodeList().clear();
                adapter.getBookNodeList().addAll(bookNodes);
                adapter.notifyDataSetChanged();
            });

        });

        View.OnClickListener onClickListener = v -> {
            binding.floatBtnMenu.collapse();
            InputPopupWindow inputPopupWindow = new InputPopupWindow(parentActivity);
            inputPopupWindow.focusWindow();
            inputPopupWindow.showAtLocation(binding.getRoot(), Gravity.CENTER, 0, 0);
            if (v.getId() == R.id.add_folder_fbt) {
                inputPopupWindow.setHint("请输入文件夹名称");
            } else {
                inputPopupWindow.setHint("请输入单词本名称");
            }
            inputPopupWindow.setConfirmBtnOnClickListener(v1 -> {
                int tag = v.getId() == R.id.add_folder_fbt ? 0 : 1;
                BookNode bookNode = BookNode.builder().nodeName(inputPopupWindow.getInputText())
                        .nodePath(bookNodeViewModel.getCurrentPath().getValue())
                        .nodeTag(tag).nodeChangedDate(new Date()).build();
                Log.i(TAG, "onCreateView: insert " + bookNode);
                bookNodeViewModel.insert(bookNode);
                inputPopupWindow.dismiss();
            });

        };

        binding.addFolderFbt.setOnClickListener(onClickListener);
        binding.addPageFbt.setOnClickListener(onClickListener);


        binding.backFolder.setOnClickListener(v -> {
            String path = "";
            String[] nodes = bookNodeViewModel.getCurrentPath().getValue().split("/");
            for (int i = 0; i < nodes.length - 1; i++) {
                path += nodes[i] + "/";
            }
            bookNodeViewModel.getCurrentPath().setValue(path);
            Log.i(TAG, "onCreateView: back to" + path);
        });

        return binding.getRoot();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            Log.w(TAG, "onAttach: not setting callback listener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void initTopBar() {
        parentActivity.setSupportActionBar(binding.toolbar);
        NavigationUI.setupActionBarWithNavController(parentActivity, navController);
        setHasOptionsMenu(true);
        if (parentActivity instanceof AppbarConfigurationSupplier) {
            NavigationUI.setupWithNavController(binding.collapsingToolbar,
                    binding.toolbar, navController,
                    ((AppbarConfigurationSupplier) parentActivity).getAppBarConfiguration());

        } else {
            throw new RuntimeException("parentActivity not implement AppbarConfigurationSupplier");
        }

    }

    private void initRecyclerView() {

        SwipeMenuCreator swipeMenuCreator =
                (leftMenu, rightMenu, position) -> {

                    SwipeMenuItem editItem =
                            new SwipeMenuItem(parentActivity)
                                    .setHeight(android.app.ActionBar.LayoutParams.MATCH_PARENT)
                                    .setWidth(150)
                                    .setText("edit")
                                    .setBackgroundColor(Color.parseColor("#23Df8B"));
                    rightMenu.addMenuItem(editItem);

                    SwipeMenuItem deleteItem =
                            new SwipeMenuItem(parentActivity)
                                    .setHeight(android.app.ActionBar.LayoutParams.MATCH_PARENT)
                                    .setWidth(150)
                                    .setBackgroundColor(Color.parseColor("#FF0000"))
                                    .setText("delete");

                    rightMenu.addMenuItem(deleteItem);
                };
        binding.bookNodeRecycler.setSwipeMenuCreator(swipeMenuCreator);

        Animation animation = AnimationUtils.loadAnimation(parentActivity, R.anim.list_item_load);
        LayoutAnimationController animationController = new LayoutAnimationController(animation);
        animationController.setOrder(LayoutAnimationController.ORDER_NORMAL);
        animationController.setDelay(0.2f);
        binding.bookNodeRecycler.setLayoutAnimation(animationController);


        LinearLayoutManager layoutManager = new LinearLayoutManager(parentActivity);
        binding.bookNodeRecycler.setLayoutManager(layoutManager);
        adapter = new BookNodeRecyclerViewAdapter(this, new ArrayList<>());

        binding.bookNodeRecycler.setAdapter(adapter);
        binding.bookNodeRecycler.hasFixedSize();
    }

    @Override
    public void onBookNodeItemClicked(int position, BookNode target) {
        Log.i(TAG, "onBookNodeItemClicked: " + target.getNodeName());
        if (target.getNodeTag() == 0) {
            bookNodeViewModel.getCurrentPath().setValue(target.getNodePath() + target.getNodeName() + "/");
        } else {
            //todo
        }

    }


    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(BookNode item);
    }
}
