package top.summus.sword.fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.matrixxun.starry.badgetextview.MaterialBadgeTextView;

import top.summus.sword.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class WordInfoFragment extends Fragment {


    public WordInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_word_info, container, false);
//        MaterialBadgeTextView materialBadgeTextView = (MaterialBadgeTextView) view.findViewById(R.id.badgeTextView);
//        materialBadgeTextView.setText("hello");
//        materialBadgeTextView.setTextColor(getResources().getColor(R.color.white));
//        materialBadgeTextView.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        return view;
    }

}
