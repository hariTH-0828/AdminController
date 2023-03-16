package edu.mobile.voterlist.api;

import java.util.List;

import edu.mobile.voterlist.model.Person;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface PersonApi {

    @POST("/api/person/setPerson")
    Call<Person> save(@Body Person person);

}
