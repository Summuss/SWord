package top.summus.sword.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

/**
 * Show a picture during loading process.
 *
 * @author Summus
 */
public class LaunchLoadingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, StartActivity.class);
        startActivity(intent);
    }
}
