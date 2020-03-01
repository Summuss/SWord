package top.summus.sword.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import top.summus.sword.R;
import top.summus.sword.databinding.ActivityTestBinding;
import top.summus.sword.fragment.TestFragment;

public class TestActivity extends AppCompatActivity {
    private static final String TAG = "TestActivity";
    public Observable<Integer> func(){
        return Observable.just(1,2,3)
                .subscribeOn(Schedulers.io());
    }

    ActivityTestBinding binding;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_test);
        replaceFragment(new TestFragment());

        func().subscribe(integer -> {
            Log.i(TAG, "onCreate: "+Thread.currentThread());
            Log.i(TAG, "onCreate: "+integer);
        });
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
