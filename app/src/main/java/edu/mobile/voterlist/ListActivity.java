package edu.mobile.voterlist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import java.util.List;
import java.util.Objects;

import edu.mobile.voterlist.api.PersonApi;
import edu.mobile.voterlist.model.Person;
import edu.mobile.voterlist.retrofit.RetrofitService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListActivity extends AppCompatActivity {
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_voters);

        Objects.requireNonNull(getSupportActionBar()).setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.white)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_launcher_back_foreground);

        listView = findViewById(R.id.listView);

        RetrofitService retrofitService = new RetrofitService();
        PersonApi personApi = retrofitService.getRetrofit().create(PersonApi.class);

        personApi.getAll().enqueue(new Callback<List<Person>>() {
            @Override
            public void onResponse(Call<List<Person>> call, Response<List<Person>> response) {
                List<Person> personList = response.body();
                setAdapterView(personList);
            }

            @Override
            public void onFailure(Call<List<Person>> call, Throwable t) {

            }
        });
    }

    public void setAdapterView(List<Person> listPerson) {
        UserAdapter userAdapter = new UserAdapter(getApplicationContext(), listPerson);
        listView.setAdapter(userAdapter);
    }

    public void onBackClicked(View view) {
        Intent switchView = new Intent(getApplicationContext(), HomeScreen.class);
        startActivity(switchView);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}