package top.summus.sword.dagger;

import android.content.Context;

import androidx.room.Room;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import top.summus.sword.SWordApplication;
import top.summus.sword.SWordDatabase;
import top.summus.sword.SWordSharedPreferences;
import top.summus.sword.api.BookNodeApi;
import top.summus.sword.api.TimeApi;
import top.summus.sword.dao.BookNodeDao;


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


    @Singleton
    @Provides
    public SWordDatabase provideDatabase(Context context) {
        return Room.databaseBuilder(context, SWordDatabase.class, "sword")
                .fallbackToDestructiveMigration()
                .build();
    }

    @Singleton
    @Provides
    public BookNodeDao provideBookNodeDao(SWordDatabase database) {
        return database.getBookNodeDao();
    }

    @Singleton
    @Provides
    public Retrofit provideRetrofit() {
        final OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(100, TimeUnit.MILLISECONDS)
                .build();
        String baseUrl = "http://192.168.1.13:8080/SwordBackend_war/";
        Gson gson = new GsonBuilder().setLenient()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    @Singleton
    @Provides
    public BookNodeApi provideBookNodeApi(Retrofit retrofit) {
        return retrofit.create(BookNodeApi.class);
    }

    @Singleton
    @Provides
    public TimeApi provideTimeApi(Retrofit retrofit) {
        return retrofit.create(TimeApi.class);
    }

}
