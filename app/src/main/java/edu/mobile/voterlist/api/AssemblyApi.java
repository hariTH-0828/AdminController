package edu.mobile.voterlist.api;


import java.util.List;

import edu.mobile.voterlist.model.Assembly;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface AssemblyApi {

    @GET("/api/assembly/district/{id}")
    Call<List<Assembly>> getIdByName(@Path("id") int id);

    @GET("/api/assembly/{id}")
    Call<Assembly> getById(@Path("id") int id);

}
