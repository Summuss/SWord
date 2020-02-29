package top.summus.sword.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

import javax.inject.Inject;

import top.summus.sword.R;
import top.summus.sword.SWordApplication;
import top.summus.sword.network.api.TimeApi;
import top.summus.sword.databinding.ActivityStartBinding;
import top.summus.sword.fragment.BaseWordListFragment;
import top.summus.sword.util.BackPressedHandle;

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
public class StartActivity extends AppCompatActivity
        implements
        BaseWordListFragment.OnFragmentInteractionListener,
        AppbarConfigurationSupplier,
        NavController.OnDestinationChangedListener, NavigationView.OnNavigationItemSelectedListener {

    private ActivityStartBinding binding;
    private NavController navController;
    private int currentFragmentId;

    /**
     * used to calculate whether to exit
     */
    private long exitTime = 0;
    private static final String TAG = "StartActivity";

    @Inject
    TimeApi timeApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_start);
        initNavController();
        binding.navView.setNavigationItemSelectedListener(this);

        SWordApplication.getAppComponent().inject(this);
        Log.i(TAG, "onCreate: " + timeApi.hashCode());

    }


    private void initNavController() {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
        navController = Objects.requireNonNull(navHostFragment).getNavController();
        NavigationUI.setupWithNavController(binding.navView, navController);

        navController.addOnDestinationChangedListener(this);
    }


    /**
     * @see AppbarConfigurationSupplier
     */
    @Override
    public AppBarConfiguration getAppBarConfiguration() {
        return new AppBarConfiguration.Builder(navController.getGraph())
                .setDrawerLayout(binding.drawerLayout).build();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {


        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
        Fragment fragment = Objects.requireNonNull(navHostFragment).getChildFragmentManager().getFragments().get(0);

        //如果按下返回键
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            //如果drawer是开的就关闭drawer,如果是关的就连按两次退出程序
            if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                binding.drawerLayout.closeDrawer(GravityCompat.START);
            } else if (navController.getGraph().getStartDestination() != currentFragmentId) {
                if (fragment instanceof BackPressedHandle) {
                    ((BackPressedHandle) fragment).onBackPressed();
                } else {
                    onBackPressed();
                }
            } else {
                if ((System.currentTimeMillis() - exitTime) > 2000) {
                    // 弹出提示，可以有多种方式
                    Snackbar.make(binding.getRoot(), "再按一次退出程序", Snackbar.LENGTH_SHORT).show();
                    exitTime = System.currentTimeMillis();
                } else {
                    finish();
                }
            }
            return true;

        } else {
            return super.onKeyDown(keyCode, event);
        }

    }


    /**
     * @see NavigationView.OnNavigationItemSelectedListener
     */
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


    /**
     * @see NavController.OnDestinationChangedListener
     */
    @Override
    public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
        currentFragmentId = destination.getId();
        if (navController.getGraph().getStartDestination() != currentFragmentId) {
            binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        } else {
            binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }
    }
}

