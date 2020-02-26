package top.summus.sword.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;



import top.summus.sword.R;
import top.summus.sword.activity.AppbarConfigurationSupplier;
import top.summus.sword.databinding.FragmentBaseWordlistBinding;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BaseWordListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BaseWordListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BaseWordListFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private static final String TAG = "BaseWordListFragment";

    private FragmentBaseWordlistBinding binding;
    private AppCompatActivity parentActivity;
    private NavController navController;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BaseWordListFragment.
     */
    public static BaseWordListFragment newInstance(String param1, String param2) {
        BaseWordListFragment fragment = new BaseWordListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);


        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_base_wordlist, container, false);
        parentActivity = (AppCompatActivity) getActivity();
        navController = NavHostFragment.findNavController(this);

        initRecyclerView();

        initTopBar();
        binding.addWordFbt.setOnClickListener(v -> {
            navController.navigate(R.id.action_baseWordListFragment_to_bookNodeFragment);
        });


        return binding.getRoot();
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
        // swipe setting

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        binding.recyclerWordList.setLayoutManager(layoutManager);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.base_word_list_fragment_menu, menu);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
    }
}
