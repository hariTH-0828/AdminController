package edu.mobile.voterlist.api;

import java.util.List;

import edu.mobile.voterlist.model.States;
import retrofit2.Call;
import retrofit2.http.GET;

public interface DistrictApi {

    @GET("/api/district/getAll")
    Call<List<States>> getAllDistrict();

}
