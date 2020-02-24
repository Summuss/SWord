package top.summus.sword.fragment;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import top.summus.sword.R;
import top.summus.sword.databinding.FragmentTestBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class TestFragment extends Fragment {

    private FragmentTestBinding binding;

    public TestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_test, container, false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        binding.testRecycler.setLayoutManager(layoutManager);

        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }
}
