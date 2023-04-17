package edu.mobile.voterlist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import edu.mobile.voterlist.api.AssemblyApi;
import edu.mobile.voterlist.api.DataFileApi;
import edu.mobile.voterlist.api.DistrictApi;
import edu.mobile.voterlist.api.PersonApi;
import edu.mobile.voterlist.api.StatesApi;
import edu.mobile.voterlist.model.Assembly;
import edu.mobile.voterlist.model.District;
import edu.mobile.voterlist.model.Person;
import edu.mobile.voterlist.model.States;
import edu.mobile.voterlist.retrofit.RetrofitService;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListActivity extends AppCompatActivity {
    ListView listView;
    View popupView;
    PopupWindow popupWindow;
    RadioButton byEpic, byAadhaar ;
    AppCompatImageView backgroundDim;
    TextInputLayout layoutAadhaar, layoutEpic;
    TextInputEditText aadhaar, epic;
    ArrayList<String> personList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_voters);

        // This fields is defined for Action Bar and Status Bar
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        int IconResId = (isDarkTheme(getApplicationContext()) ? R.drawable.ic_launcher_back_light_foreground : R.drawable.ic_launcher_back_foreground);
        getSupportActionBar().setHomeAsUpIndicator(IconResId);

        listView = findViewById(R.id.listView);
        backgroundDim = findViewById(R.id.background_dim);
        listPerson();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        return true;
    }

    public void listPerson() {
        RetrofitService retrofitService = new RetrofitService();
        PersonApi personApi = retrofitService.getRetrofit().create(PersonApi.class);

        personApi.getAll().enqueue(new Callback<List<Person>>() {
            @Override
            public void onResponse(@NonNull Call<List<Person>> call,@NonNull Response<List<Person>> response) {
                List<Person> personList = response.body();
                setAdapterView(personList);
            }
            @Override
            public void onFailure(@NonNull Call<List<Person>> call, @NonNull Throwable t) {
            }
        });
    }

    public void setAdapterView(List<Person> listPerson) {
        UserAdapter userAdapter = new UserAdapter(getApplicationContext(), listPerson);
        listView.setAdapter(userAdapter);
        Intent switchView = new Intent(getApplicationContext(), SearchResultActivity.class);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Object getPersonObject = parent.getAdapter().getItem(position);
            Person person = (Person) getPersonObject;
            personList.clear();
            getPersonByEpic(person.getEpicNumber(), switchView);
        });
    }

    public void getPersonByEpic(String epicNumber, Intent intent) {
        RetrofitService retrofitService = new RetrofitService();
        PersonApi personApi = retrofitService.getRetrofit().create(PersonApi.class);

        personApi.getPersonByEpic(epicNumber).enqueue(new Callback<Person>() {
            @Override
            public void onResponse(@NonNull Call<Person> call, @NonNull Response<Person> response) {
                if(response.isSuccessful()) {
                    Person person = response.body();
                    assert person != null;
                    personList.add(person.getName());
                    personList.add(person.getFatherName());
                    personList.add(person.getGender());
                    personList.add(person.getDateOfBirth());
                    personList.add(String.valueOf(person.getAge()));
                    personList.add(person.getPhoneNumber());
                    personList.add(person.getAadhaarNumber());
                    personList.add(person.getEpicNumber());
                    personList.add(person.getAddress());

                    intent.putStringArrayListExtra("personList", personList);

                    if(person.getImageId() != null){
                        getPersonImage(person.getImageId().getId(), intent);
                    }
                    getStateName(person.getStateId(), intent);
                    getDistrictName(person.getDistrictId(), intent);
                    getAssemblyName(person.getAssemblyId(), intent);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Person> call, @NonNull Throwable t) {
            }
        });
    }

    public void getStateName(int id, Intent intent){
        RetrofitService retrofitService = new RetrofitService();
        StatesApi statesApi = retrofitService.getRetrofit().create(StatesApi.class);
        statesApi.getStatesById(id).enqueue(new Callback<States>() {
            @Override
            public void onResponse(@NonNull Call<States> call, @NonNull Response<States> response) {
                States states = Objects.requireNonNull(response.body());
                intent.putExtra("stateName", states.getState());
            }
            @Override
            public void onFailure(@NonNull Call<States> call, @NonNull Throwable t) {
            }
        });
    }

    public void getDistrictName(int id, Intent intent){
        RetrofitService retrofitService = new RetrofitService();
        DistrictApi districtApi = retrofitService.getRetrofit().create(DistrictApi.class);

        districtApi.getDistrictById(id).enqueue(new Callback<District>() {
            @Override
            public void onResponse(@NonNull Call<District> call, @NonNull Response<District> response) {
                District district = Objects.requireNonNull(response.body());
                intent.putExtra("districtName", district.getDistrict());
            }
            @Override
            public void onFailure(@NonNull Call<District> call, @NonNull Throwable t) {

            }
        });
    }

    public void getAssemblyName(int id, Intent intent) {
        RetrofitService retrofitService = new RetrofitService();
        AssemblyApi assemblyApi = retrofitService.getRetrofit().create(AssemblyApi.class);

        assemblyApi.getById(id).enqueue(new Callback<Assembly>() {
            @Override
            public void onResponse(@NonNull Call<Assembly> call, @NonNull Response<Assembly> response) {
                Assembly assembly = Objects.requireNonNull(response.body());
                intent.putExtra("assemblyName", assembly.getAssembly());
                startActivity(intent);
            }
            @Override
            public void onFailure(@NonNull Call<Assembly> call, @NonNull Throwable t) {}
        });
    }

    private void getPersonImage(long id, Intent intent) {
        RetrofitService retrofitService = new RetrofitService();
        DataFileApi dataFileApi = retrofitService.getRetrofit().create(DataFileApi.class);

        dataFileApi.getUserProfileImage(id).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if(response.isSuccessful()) {
                    InputStream inputStream = Objects.requireNonNull(response.body()).byteStream();
                    Bitmap bitmapImage = BitmapFactory.decodeStream(inputStream);

                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    bitmapImage.compress(Bitmap.CompressFormat.JPEG, 20, outputStream);
                    byte[] byteArray = outputStream.toByteArray();

                    intent.putExtra("bitmap", byteArray);
                }
            }
            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Toast.makeText(getApplicationContext(), "Image fetch failed", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public boolean isDarkTheme(Context context) {
        int nightMode = context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        return nightMode == Configuration.UI_MODE_NIGHT_YES;
    }

    @SuppressLint("InflateParams")
    public void onSearchActionClicked() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        popupView = inflater.inflate(R.layout.search_dialogue_layout, null);
        byEpic = popupView.findViewById(R.id.searchByEpic);
        byAadhaar = popupView.findViewById(R.id.searchByAadhaar);
        backgroundDim.setVisibility(View.VISIBLE);

        layoutEpic = popupView.findViewById(R.id.layoutEpic);
        layoutAadhaar = popupView.findViewById(R.id.layoutAadhaar);
        aadhaar = popupView.findViewById(R.id.aadhaarInputTextView);
        epic = popupView.findViewById(R.id.epicInputTextView);

        popupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        popupWindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);

        popupWindow.setOnDismissListener(() -> backgroundDim.setVisibility(View.INVISIBLE));
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

    public void onAadhaarClick(View view) {
        Intent switchView = new Intent(getApplicationContext(), SearchResultActivity.class);
        String aadhaarNumber = Objects.requireNonNull(aadhaar.getText()).toString();
        getPersonByAadhaar(aadhaarNumber, switchView);
    }

    public void onEpicClick(View view) {
        Intent switchView = new Intent(getApplicationContext(), SearchResultActivity.class);
        String epicNumber = Objects.requireNonNull(epic.getText()).toString();
        personList.clear();
        getPersonByEpic(epicNumber, switchView);
    }

    public void getPersonByAadhaar(String aadhaarNumber, Intent intent) {
        RetrofitService retrofitService = new RetrofitService();
        PersonApi personApi = retrofitService.getRetrofit().create(PersonApi.class);

        personApi.getPersonByAadhaar(aadhaarNumber).enqueue(new Callback<Person>() {
            @Override
            public void onResponse(@NonNull Call<Person> call,@NonNull Response<Person> response) {
                if(response.isSuccessful() && response.body() != null){
                    Person person = response.body();
                    personList.add(person.getName());
                    personList.add(person.getFatherName());
                    personList.add(person.getGender());
                    personList.add(person.getDateOfBirth());
                    personList.add(String.valueOf(person.getAge()));
                    personList.add(person.getPhoneNumber());
                    personList.add(person.getAadhaarNumber());
                    personList.add(person.getEpicNumber());
                    personList.add(person.getAddress());

                    intent.putStringArrayListExtra("personList", personList);

                    getStateName(person.getStateId(), intent);
                    getDistrictName(person.getDistrictId(), intent);
                    getAssemblyName(person.getAssemblyId(), intent);
                    if(person.getImageId() != null){
                        getPersonImage(person.getImageId().getId(), intent);
                    }
                }
            }
            @Override
            public void onFailure(@NonNull Call<Person> call, @NonNull Throwable t) {
            }
        });
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_search:
                onSearchActionClicked();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}