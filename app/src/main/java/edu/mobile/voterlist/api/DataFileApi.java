package edu.mobile.voterlist.api;

import edu.mobile.voterlist.model.DataFileInfo;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface DataFileApi {

    @Multipart
    @POST("/api/uploadFile")
    Call<DataFileInfo> save(@Part MultipartBody.Part file);
}
