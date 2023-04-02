package edu.mobile.voterlist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
import edu.mobile.voterlist.model.Assembly;
import edu.mobile.voterlist.model.District;
import edu.mobile.voterlist.model.Person;
import edu.mobile.voterlist.model.States;
import edu.mobile.voterlist.retrofit.RetrofitService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {

    RadioButton byEpic, byAadhaar;
    TextInputLayout layoutAadhaar, layoutEpic;
    TextInputEditText aadhaar, epic;
    ArrayList<String> personList = new ArrayList<>();

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

    public void onAadhaarClick(View view) throws IOException {
        Intent switchView = new Intent(getApplicationContext(), SearchResultActivity.class);
        String aadhaarNumber = aadhaar.getText().toString();
        getPerson(aadhaarNumber, switchView);
    }

    public void getPerson(String aadhaarNumber, Intent intent) {
        RetrofitService retrofitService = new RetrofitService();
        PersonApi personApi = retrofitService.getRetrofit().create(PersonApi.class);

        personApi.getPersonByAadhaar(aadhaarNumber).enqueue(new Callback<Person>() {
            @Override
            public void onResponse(@NonNull Call<Person> call,@NonNull Response<Person> response) {
                if(response.isSuccessful()){
                    Person person = response.body();
                    personList.add(person.getName());
                    personList.add(person.getFatherName());
                    personList.add(person.getGender());
                    personList.add(person.getDateOfBirth());
                    personList.add(String.valueOf(person.getAge()));
                    personList.add(person.getPhoneNumber());
                    personList.add(person.getAadhaarNumber());
                    personList.add(person.getEpicNumber());

                    intent.putStringArrayListExtra("personList", personList);

                    getStateName(person.getStateId(), intent);
                    getDistrictName(person.getDistrictId(), intent);
                    getAssemblyName(person.getAssemblyId(), intent);
                }
            }
            @Override
            public void onFailure(Call<Person> call, Throwable t) {
            }
        });
    }

    public void getStateName(int id, Intent intent){
        RetrofitService retrofitService = new RetrofitService();
        StatesApi statesApi = retrofitService.getRetrofit().create(StatesApi.class);
        statesApi.getStatesById(id).enqueue(new Callback<States>() {
            @Override
            public void onResponse(Call<States> call, Response<States> response) {
                States states = response.body();
                intent.putExtra("stateName", states.getState());

            }
            @Override
            public void onFailure(Call<States> call, Throwable t) {
            }
        });
    }

    public void getDistrictName(int id, Intent intent){
        RetrofitService retrofitService = new RetrofitService();
        DistrictApi districtApi = retrofitService.getRetrofit().create(DistrictApi.class);

        districtApi.getDistrictById(id).enqueue(new Callback<District>() {
            @Override
            public void onResponse(Call<District> call, Response<District> response) {
                District district = response.body();
                intent.putExtra("districtName", district.getDistrict());
            }
            @Override
            public void onFailure(Call<District> call, Throwable t) {

            }
        });
    }

    public void getAssemblyName(int id, Intent intent) {
        RetrofitService retrofitService = new RetrofitService();
        AssemblyApi assemblyApi = retrofitService.getRetrofit().create(AssemblyApi.class);

        assemblyApi.getById(id).enqueue(new Callback<Assembly>() {
            @Override
            public void onResponse(Call<Assembly> call, Response<Assembly> response) {
                Assembly assembly = response.body();
                intent.putExtra("assemblyName", assembly.getAssembly());
                startActivity(intent);
            }
            @Override
            public void onFailure(Call<Assembly> call, Throwable t) {}
        });
    }
}