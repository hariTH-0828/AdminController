package edu.mobile.voterlist.api;

import java.util.List;

import edu.mobile.voterlist.model.Parties;
import retrofit2.Call;
import retrofit2.http.GET;

public interface PartiesApi {

    @GET("/api/parties/getAll")
    Call<List<Parties>> getAllParties();
}
