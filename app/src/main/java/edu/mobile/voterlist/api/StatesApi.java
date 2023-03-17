package edu.mobile.voterlist.api;

import java.util.List;

import edu.mobile.voterlist.model.States;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface StatesApi {

    @GET("/api/states/getAll")
    Call<List<States>> getAllStates();

    @GET("/api/states/search/{stateName}")
    Call<Integer> getStateIdByName(@Path("stateName") String stateName);
}
