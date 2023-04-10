package edu.mobile.voterlist.api;

import java.util.List;

import edu.mobile.voterlist.model.Person;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PersonApi {

    @POST("/api/person/setPerson")
    Call<Person> save(@Body Person person);

    @GET("/api/person/getAll")
    Call<List<Person>> getAll();

    @GET("/api/person/aadhaar/{aadhaar_number}")
    Call<Person> getPersonByAadhaar(@Path("aadhaar_number") String aadhaarNumber);

    @DELETE("/api/person/{id}")
    Call<String> deletePerson(@Path("id") int personId);

    @GET("/api/person/epic/{epic_number}")
    Call<Person> getPersonByEpic(@Path("epic_number") String epicNumber);

    @PUT("/api/person/updateProfile/{id}")
    Call<Boolean> associateProfilePhoto(@Path("id") int personId, @Query("imageId") long imageId);

    @PUT("/api/person/{id}")
    Call<Person> updatePersonById(@Path("id") int personId, @Body Person existPerson);
}
