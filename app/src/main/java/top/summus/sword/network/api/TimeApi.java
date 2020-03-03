package top.summus.sword.network.api;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;

public interface TimeApi {
    @GET("time")
    Observable<Response<Void>> getTime();

    @GET("earliestDateOfDeleteRecord")
    Observable<Response<Void>> getEarliestDateOfDeleteRecord();
}
