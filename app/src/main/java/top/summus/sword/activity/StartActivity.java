package top.summus.sword.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

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

    }


    @Override
    public AppBarConfiguration getAppBarConfiguration() {
        return new AppBarConfiguration.Builder(navController.getGraph())
                .setDrawerLayout(binding.drawerLayout).build();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

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
                    Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                    exitTime = System.currentTimeMillis();
                } else {
                    finish();
                }
            }
        }
        return true;

    }
}
