package top.summus.sword.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.List;

import top.summus.sword.R;
import top.summus.sword.databinding.ActivityStartBinding;
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
public class StartActivity extends AppCompatActivity implements BaseWordListFragment.OnFragmentInteractionListener, AppbarConfigurationSupplier {
    private ActivityStartBinding binding;
    private NavController navController;

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
        initAppBar();

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            NavHostFragment hostFragment = (NavHostFragment) getSupportFragmentManager().getFragments().get(0);

            List<Fragment> fragments = hostFragment.getChildFragmentManager().getFragments();
            Log.i(TAG, "onCreate: " + fragments.size());
            if (!fragments.isEmpty()) {
                Log.i(TAG, "onCreate: " + fragments.get(0).getClass());
            }

        });

    }

    /**
     * appbar setting to make top bar have proper action.
     * waring: the sequence of method calling matters very much.
     */
    private void initAppBar() {


//        setSupportActionBar(binding.toolbar);
        // relate supportActionBar with navController
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        NavigationUI.setupWithNavController(binding.navView, navController);
//        NavigationUI.setupWithNavController(binding.collapsingToolbar, binding.toolbar, navController, appBarConfiguration);

    }

    @Override
    public AppBarConfiguration getAppBarConfiguration() {
        return new AppBarConfiguration.Builder(navController.getGraph())
                .setDrawerLayout(binding.drawerLayout).build();
    }



    /*
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //如果按下返回键
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            //如果drawer是开的就关闭drawer,如果是关的就连按两次退出程序
            if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                binding.drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                if ((System.currentTimeMillis() - exitTime) > 2000) {
                    // 弹出提示，可以有多种方式
                    Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                    exitTime = System.currentTimeMillis();
                } else {
                    finish();
                }
            }
        }
        return true;

    }
*/
}
