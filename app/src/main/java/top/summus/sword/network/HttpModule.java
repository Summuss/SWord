package top.summus.sword.network;

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
import top.summus.sword.SWordSharedPreferences;
import top.summus.sword.network.api.BookNodeApi;
import top.summus.sword.network.api.TimeApi;
import top.summus.sword.network.service.BookNodeHttpService;
import top.summus.sword.room.service.BookNodeRoomService;

@Module
public class HttpModule {
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

    @Singleton
    @Provides
    public BookNodeHttpService provideBookNodeHttpServicee(BookNodeApi bookNodeApi, BookNodeRoomService bookNodeRoomService, SWordSharedPreferences sWordSharedPreferences) {
        return new BookNodeHttpService(bookNodeApi, bookNodeRoomService, sWordSharedPreferences);
    }

}
