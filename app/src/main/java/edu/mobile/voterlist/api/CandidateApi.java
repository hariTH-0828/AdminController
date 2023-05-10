package edu.mobile.voterlist.api;

import java.util.List;

import edu.mobile.voterlist.model.Candidate;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface CandidateApi {

    @GET("/api/Candidates/getAll")
    Call<List<Candidate>> getAllCandidates();

    @POST("/api/Candidates/save")
    Call<Candidate> saveCandidates(@Body Candidate candidate);
}
