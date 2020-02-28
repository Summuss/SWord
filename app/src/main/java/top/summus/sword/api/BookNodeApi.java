package top.summus.sword.api;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.*;
import top.summus.sword.entity.BookNode;

import java.util.List;

public interface BookNodeApi {


    @GET("time")
    Call<Void> time();


    @GET("bookNodes/{changedDate}")
    Observable<Response<List<BookNode>>> downLoadUnSynced(@Path("changedDate") String date);

    @GET("bookNodes")
    Observable<Response<List<BookNode>>> getAll();

    @POST("bookNodes")
    Observable<Response<Integer>> postBookNode(@Body BookNode bookNode);

    @PATCH("bookNodes")
    Observable<Response<Integer>> patchBookNode(@Body BookNode bookNode);


}
