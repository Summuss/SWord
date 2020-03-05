package top.summus.sword.network.api;



import java.util.List;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import top.summus.sword.room.entity.DeleteRecord;

public interface DeleteRecordApi {
    @GET("deleteRecords/{lastDeleteRecordSyncDate}")
    Observable<Response<List<DeleteRecord>>> getAll(@Path("lastDeleteRecordSyncDate") String date);

    @POST("deleteRecords")
    Observable<Response<Void>> post(@Body DeleteRecord deleteRecord);


}
