package edu.mobile.voterlist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.util.ArrayList;

import edu.mobile.voterlist.api.AssemblyApi;
import edu.mobile.voterlist.api.DistrictApi;
import edu.mobile.voterlist.api.PersonApi;
import edu.mobile.voterlist.api.StatesApi;
import edu.mobile.voterlist.model.Person;
import edu.mobile.voterlist.retrofit.RetrofitService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {

    RadioButton byEpic, byAadhaar;
    TextInputLayout layoutAadhaar, layoutEpic;
    TextInputEditText aadhaar, epic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        byEpic = findViewById(R.id.searchByEpic);
        byAadhaar = findViewById(R.id.searchByAadhaar);
        layoutAadhaar = findViewById(R.id.layoutAadhaar);
        layoutEpic = findViewById(R.id.layoutEpic);

        aadhaar = findViewById(R.id.editInputAadhaar);
        epic = findViewById(R.id.editInputEpic);
    }

    public void onSearchByClick(View view) {

        byEpic.setOnClickListener(v -> {
            byEpic.setChecked(true);
            byAadhaar.setChecked(false);

            layoutEpic.setVisibility(View.VISIBLE);
            layoutAadhaar.setVisibility(View.INVISIBLE);
        });

        byAadhaar.setOnClickListener(v -> {
            byAadhaar.setChecked(true);
            byEpic.setChecked(false);

            layoutEpic.setVisibility(View.INVISIBLE);
            layoutAadhaar.setVisibility(View.VISIBLE);

        });
        byEpic.setChecked(false);
        layoutEpic.setVisibility(View.INVISIBLE);
        layoutAadhaar.setVisibility(View.VISIBLE);
    }

    public void onBackClicked(View view) {
        Intent switchView = new Intent(getApplicationContext(), HomeScreen.class);
        startActivity(switchView);
    }

    public void onAadhaarClick(View view) {
        Intent switchView = new Intent(getApplicationContext(), SearchResultActivity.class);
        String aadhaarNumber = aadhaar.getText().toString();

        RetrofitService retrofitService = new RetrofitService();
        PersonApi personApi = retrofitService.getRetrofit().create(PersonApi.class);
        personApi.getPersonByAadhaar(aadhaarNumber).enqueue(new Callback<Person>() {
            @Override
            public void onResponse(@NonNull Call<Person> call,@NonNull Response<Person> response) {
                ArrayList<String> personDetail = new ArrayList<>();
                Person person = response.body();

                personDetail.add(person.getName());

                switchView.putStringArrayListExtra("PersonList", personDetail);
                startActivity(switchView);
            }
            @Override
            public void onFailure(Call<Person> call, Throwable t) {

            }
        });
    }
}