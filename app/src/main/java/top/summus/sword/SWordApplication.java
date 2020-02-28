package top.summus.sword;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import top.summus.sword.dagger.AppComponent;
import top.summus.sword.dagger.DaggerAppComponent;


public class SWordApplication extends Application {

    @SuppressLint("StaticFieldLeak")
    private static Context context;
    private static AppComponent appComponent;



    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        appComponent = DaggerAppComponent.create();

    }

    public static Context getContext() {
        return context;
    }

    public static AppComponent getAppComponent() {
        return appComponent;
    }
}
