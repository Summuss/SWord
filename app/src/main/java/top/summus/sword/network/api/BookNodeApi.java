package top.summus.sword.network.api;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.*;
import top.summus.sword.room.entity.BookNode;

import java.util.List;

public interface BookNodeApi {


    @GET("time")
    Call<Void> time();


    @GET("bookNodes/{changedDate}")
    Observable<Response<List<BookNode>>> downLoadUnSynced(@Path("changedDate") String date);

    @GET("bookNodes")
    Observable<Response<List<BookNode>>> getAll();

    @POST("bookNodes")
    Single<Response<Integer>> postBookNode(@Body BookNode bookNode);

    @PATCH("bookNodes")
    Single<Response<Integer>> patchBookNode(@Body BookNode bookNode);


}
