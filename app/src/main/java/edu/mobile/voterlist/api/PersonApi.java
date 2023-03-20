package edu.mobile.voterlist.api;

import edu.mobile.voterlist.model.Person;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface PersonApi {

    @POST("/api/person/setPerson")
    Call<Person> save(@Body Person person);

    @GET("/api/person/phone/{phone_number}")
    Call<Boolean> isExist(@Path("phone_number") String phoneNumber);
}
