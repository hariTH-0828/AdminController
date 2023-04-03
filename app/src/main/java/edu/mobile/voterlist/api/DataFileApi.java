package edu.mobile.voterlist.api;

import edu.mobile.voterlist.model.DataFileInfo;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface DataFileApi {

    @Multipart
    @POST("/api/uploadFile")
    Call<DataFileInfo> save(@Part MultipartBody.Part file);
    @GET("/api/uploadFile/{id}/content")
    Call<ResponseBody> getUserProfileImage(@Path("id") long id);

    @GET("/api/uploadFile/{id}")
    Call<DataFileInfo> getUserProfile(@Path("id") int id);
}
