package top.summus.sword.fragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.view.animation.OvershootInterpolator;

import com.scwang.smartrefresh.layout.header.BezierRadarHeader;
import com.yanzhenjie.recyclerview.OnItemMenuClickListener;
import com.yanzhenjie.recyclerview.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.SwipeMenuItem;

import java.util.Date;


import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import top.summus.sword.R;
import top.summus.sword.activity.AppbarConfigurationSupplier;
import top.summus.sword.adapter.BookNodeRecyclerViewAdapter;
import top.summus.sword.component.InputPopupWindow;
import top.summus.sword.databinding.FragmentBooknodeBinding;
import top.summus.sword.room.entity.BookNode;
import top.summus.sword.util.BackPressedHandle;
import top.summus.sword.viewmodel.BookNodeViewModel;


public class BookNodeFragment extends Fragment implements BookNodeRecyclerViewAdapter.OnBookNodeItemClicked, BookNodeViewModel.DataChangedListener, OnItemMenuClickListener, BackPressedHandle {

    private static final String TAG = "BookNodeFragment";
    private FragmentBooknodeBinding binding;
    private AppCompatActivity parentActivity;
    private NavController navController;
    private BookNodeViewModel bookNodeViewModel;
    private BookNodeRecyclerViewAdapter adapter;


    private void initMember() {
        parentActivity = (AppCompatActivity) getActivity();
        navController = NavHostFragment.findNavController(this);
        bookNodeViewModel = BookNodeViewModel.getInstance(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_booknode, container, false);
        binding.setFragment(this);
        initMember();
        initTopBar();
        initRecyclerView();
        binding.refreshLayout.setRefreshHeader(new BezierRadarHeader(getActivity()).setEnableHorizontalDrag(true));
        binding.refreshLayout.setOnRefreshListener(refreshLayout -> {
            Log.i(TAG, "onRefresh: ");
            try {
                bookNodeViewModel.sync(() -> {
                    Log.i(TAG, "onRefresh: ok");
                    refreshLayout.finishRefresh();

                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

//        binding.refreshLayout.setOnRefreshListener(new OnRefreshListener() {
//            @Override
//            public void onRefresh(RefreshLayout refreshlayout) {
//                Log.i(TAG, "onRefresh: ");
//                try {
//                    bookNodeViewModel.sync(() -> {
//                        Log.i(TAG, "onRefresh: ok");
////                        refreshlayout.finishRefresh(100,true,false);
//                        refreshlayout.finishRefresh(/*,false*/);//传入false表示刷新失败
//
//                    });
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//        });
        return binding.getRoot();
    }


    private void initTopBar() {
        parentActivity.setSupportActionBar(binding.toolbar);
        NavigationUI.setupActionBarWithNavController(parentActivity, navController);
        binding.toolbar.setTitle("");
        setHasOptionsMenu(true);
        if (parentActivity instanceof AppbarConfigurationSupplier) {
            NavigationUI.setupWithNavController(binding.collapsingToolbar,
                    binding.toolbar, navController,
                    ((AppbarConfigurationSupplier) parentActivity).getAppBarConfiguration());

        } else {
            throw new RuntimeException("parentActivity not implement AppbarConfigurationSupplier");
        }

    }

    private void initSwipeMenu() {
        //set swipe menu
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
                                    .setText("deleteIntoDeleteRecord");

                    rightMenu.addMenuItem(deleteItem);
                };
        binding.bookNodeRecycler.setSwipeMenuCreator(swipeMenuCreator);
        binding.bookNodeRecycler.setOnItemMenuClickListener(this);

    }

    private void initAdapter() {
        //set adapter
        adapter = new BookNodeRecyclerViewAdapter(this, bookNodeViewModel.getBookNodesShowed());
        ScaleInAnimationAdapter animationAdapter = new ScaleInAnimationAdapter(adapter);
        animationAdapter.setDuration(1000);
        animationAdapter.setFirstOnly(false);
        animationAdapter.setInterpolator(new OvershootInterpolator());
        binding.bookNodeRecycler.setAdapter(animationAdapter);
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(parentActivity);
        binding.bookNodeRecycler.setLayoutManager(layoutManager);
        initSwipeMenu();
        initAdapter();
        binding.bookNodeRecycler.setHasFixedSize(true);
    }


    public void onAddNodeFbtClick(View v) {
        binding.floatBtnMenu.collapse();
        InputPopupWindow inputPopupWindow = new InputPopupWindow(parentActivity);
        inputPopupWindow.showAtLocation(binding.getRoot(), Gravity.CENTER, 0, 0);
        inputPopupWindow.focusWindow();
        if (v.getId() == R.id.add_folder_fbt) {
            inputPopupWindow.setHint("请输入文件夹名称");
        } else {
            inputPopupWindow.setHint("请输入单词本名称");
        }
        inputPopupWindow.setConfirmBtnOnClickListener(v1 -> {
            int tag = v.getId() == R.id.add_folder_fbt ? 0 : 1;
            BookNode bookNode = BookNode.builder().nodeName(inputPopupWindow.getInputText())
                    .nodePath(bookNodeViewModel.getCurrentPath())
                    .nodeTag(tag).nodeChangedDate(new Date()).build();
            Log.i(TAG, "onCreateView: insert " + bookNode);
            bookNodeViewModel.insert(bookNode);
            inputPopupWindow.dismiss();

        });

    }

    public void onBackToPreviousClick(View v) {
        String path = "";
        String[] nodes = bookNodeViewModel.getCurrentPath().split("/");
        for (int i = 0; i < nodes.length - 1; i++) {
            path += nodes[i] + "/";
        }
        bookNodeViewModel.switchPath(path);
    }

    /**
     * {@link BookNodeRecyclerViewAdapter.OnBookNodeItemClicked}
     */
    @Override
    public void onBookNodeItemClicked(int position, BookNode target) {
        Log.i(TAG, "onBookNodeItemClicked: " + target.getNodeName());
        if (target.getNodeTag() == 0) {
            bookNodeViewModel.switchPath(target.getNodePath() + target.getNodeName() + "/");
        } else {
            Bundle bundle = new Bundle();
            bundle.putSerializable("bookNode", target);
            navController.navigate(R.id.action_bookNodeFragment_to_wordFragment2, bundle);
        }

    }

    /**
     * {@link BookNodeViewModel.DataChangedListener}
     **/
    @Override
    public void onPathSwished(String destinationPath) {
        if ("/".equals(destinationPath)) {
            binding.backToPrevious.setVisibility(View.GONE);
        } else {
            binding.backToPrevious.setVisibility(View.VISIBLE);
        }
        adapter.notifyDataSetChanged();
        binding.pathTitle.setText(bookNodeViewModel.getCurrentPath());
        binding.pathTitle.setMovementMethod(ScrollingMovementMethod.getInstance());
        int length = binding.pathTitle.getText().length();
        binding.pathTitle.setSelection(length, length);
    }

    /**
     * {@link BookNodeViewModel.DataChangedListener}
     **/
    @Override
    public void onInsertFinished(int position) {
        adapter.notifyItemInserted(position);
        binding.bookNodeRecycler.smoothScrollToPosition(position);
    }


    /**
     * {@link BookNodeViewModel.DataChangedListener}
     **/
    @Override
    public void onDeleteFinished(int position) {
        Log.i(TAG, "onDeleteFinished: deleteIntoDeleteRecord position " + position);
        adapter.notifyItemRemoved(position);
    }

    /**
     * the listener of recyclerView's swipe menu
     * {@link OnItemMenuClickListener}
     */
    @Override
    public void onItemClick(SwipeMenuBridge menuBridge, int adapterPosition) {
        // 左侧还是右侧菜单：
        int direction = menuBridge.getDirection();
        // 菜单在Item中的Position：
        int menuPosition = menuBridge.getPosition();
        BookNode bookNode = adapter.getBookNodeList().get(adapterPosition);
        if (direction == -1) {
            if (menuPosition == 1) {
                bookNodeViewModel.delete(bookNode, adapterPosition
                );

            } else if (menuPosition == 0) {
                //todo
            }
        }
        binding.bookNodeRecycler.smoothCloseMenu();
    }

//    public OnRefreshListener refreshListener() {
//
//        return refreshLayout -> {
//            Log.i(TAG, "onRefresh: ");
//            try {
//                bookNodeViewModel.sync(() -> {
//                    Log.i(TAG, "onRefresh: ok");
//                    refreshLayout.finishRefresh();
//
//                });
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        };
//
//    }

    @Override
    public void onBackPressed() {
        Log.i(TAG, "onBackPressed");
        if (binding.floatBtnMenu.isExpanded()) {
            binding.floatBtnMenu.collapse();
        } else if ("/".equals(bookNodeViewModel.getCurrentPath())) {
            parentActivity.onBackPressed();
        } else {
            onBackToPreviousClick(null);
        }
    }
}
