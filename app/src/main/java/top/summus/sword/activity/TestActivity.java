package top.summus.sword.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import top.summus.sword.R;
import top.summus.sword.databinding.ActivityTestBinding;
import top.summus.sword.fragment.TestFragment;

public class TestActivity extends AppCompatActivity {

    ActivityTestBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_test);
        replaceFragment(new TestFragment());
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.right_layout, fragment);
//    transaction.add(R.id.right_layout,fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
