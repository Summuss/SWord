package top.summus.sword.dagger;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import top.summus.sword.SWordApplication;
import top.summus.sword.SWordSharedPreferences;


@Module
public class SingletonModule {


    @Singleton
    @Provides
    public Context provideContext() {
        return SWordApplication.getContext();
    }

    @Singleton
    @Provides
    public SWordSharedPreferences provideSWordSharedPreferences(Context context) {
        return new SWordSharedPreferences(context);
    }


}
