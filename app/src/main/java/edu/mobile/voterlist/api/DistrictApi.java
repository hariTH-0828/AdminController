package edu.mobile.voterlist.api;

import java.util.List;

import edu.mobile.voterlist.model.District;
import edu.mobile.voterlist.model.States;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface DistrictApi {

    @GET("/api/district/state/{stateId}")
    Call<List<District>> getDistrictByStateId(@Path("stateId") int stateId);

    @GET("/api/district/search/{districtName}")
    Call<Integer> getDistrictIdByName(@Path("districtName") String districtName);

    @GET("/api/district/{id}")
    Call<District> getDistrictById(@Path("id") int id);

}
