package top.summus.sword.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;

import top.summus.sword.R;
import top.summus.sword.databinding.ActivityStartBinding;

/**
 * Launched after {@link LaunchLoadingActivity} finished
 *
 * <p>{@code StartActivity} use {@link DrawerLayout} as its main layout.
 * {@link DrawerLayout} consist of two part:
 *      <ol>
 *          <li>left drawer</li>
 *          <li>right fragment</li>
 *      </ol>
 *
 * @author Summus
 */
public class StartActivity extends AppCompatActivity {
    private ActivityStartBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_start);

    }
}
