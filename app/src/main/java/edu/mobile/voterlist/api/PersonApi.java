package edu.mobile.voterlist.api;

import edu.mobile.voterlist.model.Person;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PersonApi {

    @POST("/api/person/setPerson")
    Call<Person> save(@Body Person person);

    @GET("/api/person/phone/{phone_number}")
    Call<Boolean> isExist(@Path("phone_number") String phoneNumber);

    @GET("/api/person/aadhaar/{aadhaar_number}")
    Call<Person> getPersonByAadhaar(@Path("aadhaar_number") String aadhaarNumber);

    @GET("/api/person/epic/{epic_number}")
    Call<Person> getPersonByEpic(@Path("epic_number") String epicNumber);

    @PUT("/api/person/updateProfile/{id}")
    Call<Boolean> associateProfilePhoto(@Path("id") int personId, @Query("imageId") long imageId);
}
