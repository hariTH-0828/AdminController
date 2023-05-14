package edu.mobile.voterlist.api;

import java.util.List;

import edu.mobile.voterlist.model.Candidate;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CandidateApi {

    @GET("/api/Candidates/getAll")
    Call<List<Candidate>> getAllCandidates();

    @PUT("/api/Candidates/updateProfile/{id}")
    Call<Boolean> associateProfilePhoto(@Path("id") int candidateId, @Query("imageId") long imageId);
    @POST("/api/Candidates/save")
    Call<Candidate> saveCandidates(@Body Candidate candidate);
}
