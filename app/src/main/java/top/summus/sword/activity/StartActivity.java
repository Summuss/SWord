package top.summus.sword.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import io.reactivex.CompletableObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import top.summus.sword.R;
import top.summus.sword.SWordDatabase;
import top.summus.sword.dao.BookNodeDao;
import top.summus.sword.databinding.ActivityStartBinding;
import top.summus.sword.entity.BookNode;
import top.summus.sword.fragment.BaseWordListFragment;

/**
 * Launched after {@link LaunchLoadingActivity} finished
 *
 * <p>{@code StartActivity} use {@link DrawerLayout} as its main layout.
 * {@link DrawerLayout} consist of two part:
 * <ol>
 * <li>left drawer</li>
 * <li>right fragment</li>
 * </ol>
 *
 * @author Summus
 */
public class StartActivity extends AppCompatActivity implements BaseWordListFragment.OnFragmentInteractionListener, AppbarConfigurationSupplier, NavigationView.OnNavigationItemSelectedListener {
    private ActivityStartBinding binding;
    private NavController navController;
    private int currentFragmentId;

    /**
     * used to calculate whether to exit
     */
    private long exitTime = 0;
    private static final String TAG = "StartActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_start);

        navController = Navigation.findNavController(this, R.id.fragment);
        NavigationUI.setupWithNavController(binding.navView, navController);

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> currentFragmentId = destination.getId());

        binding.navView.setNavigationItemSelectedListener(this);


    }





    @Override
    public AppBarConfiguration getAppBarConfiguration() {
        return new AppBarConfiguration.Builder(navController.getGraph())
                .setDrawerLayout(binding.drawerLayout).build();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
        List<Fragment> fragments = navHostFragment.getChildFragmentManager().getFragments();

        //如果按下返回键
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            //如果drawer是开的就关闭drawer,如果是关的就连按两次退出程序
            if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                binding.drawerLayout.closeDrawer(GravityCompat.START);
            } else if (navController.getGraph().getStartDestination() != currentFragmentId) {
                onBackPressed();
            } else {
                if ((System.currentTimeMillis() - exitTime) > 2000) {
                    // 弹出提示，可以有多种方式
                    Snackbar.make(binding.getRoot(), "再按一次退出程序", Snackbar.LENGTH_SHORT).show();
                    exitTime = System.currentTimeMillis();
                } else {
                    finish();
                }
            }
        }
        return true;

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        }
        if (item.getItemId() == R.id.bookNodeFragment_item) {
            navController.navigate(R.id.action_baseWordListFragment_to_bookNodeFragment);
        } else if (item.getItemId() == R.id.testFragment) {
            navController.navigate(R.id.action_baseWordListFragment_to_testFragment);
        }
        return true;
    }

}
