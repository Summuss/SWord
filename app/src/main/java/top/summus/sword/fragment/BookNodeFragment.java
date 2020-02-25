package top.summus.sword.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yanzhenjie.recyclerview.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.SwipeMenuItem;

import java.util.ArrayList;


import top.summus.sword.R;
import top.summus.sword.activity.AppbarConfigurationSupplier;
import top.summus.sword.adapter.BookNodeRecyclerViewAdapter;
import top.summus.sword.databinding.FragmentBooknodeBinding;
import top.summus.sword.entity.BookNode;
import top.summus.sword.viewmodel.BookNodeViewModel;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class BookNodeFragment extends Fragment {

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
        bookNodeViewModel.getBookNodesShowed().observe(parentActivity, bookNodes -> {
            adapter.getBookNodeList().clear();
            adapter.getBookNodeList().addAll(bookNodes);
            adapter.notifyDataSetChanged();
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

        LinearLayoutManager layoutManager = new LinearLayoutManager(parentActivity);
        binding.bookNodeRecycler.setLayoutManager(layoutManager);
        adapter = new BookNodeRecyclerViewAdapter(new ArrayList<>());
//        binding.recyclerWordList.setOnLongClickListener(this);
        binding.bookNodeRecycler.setAdapter(adapter);
        binding.bookNodeRecycler.hasFixedSize();
    }


    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(BookNode item);
    }
}
