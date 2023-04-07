package edu.mobile.voterlist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;
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

public class UpdateActivity extends AppCompatActivity {

    TextInputEditText inputVoterId;
    Button submitButton, cancelButton, updateButton;
    ScrollView resultView;
    Person person;
    States states;
    District district;
    Assembly assembly;
    TextView viewName, viewVoterId, viewFatherName, viewGender, viewDob, viewAge, viewPhoneNo, viewAadhaarNo, viewAddress, viewState, viewDistrict, viewAssembly;
    CircleImageView viewVoterImage;
    String voterName, voterDob;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        int IconResId = (isDarkTheme(getApplicationContext()) ? R.drawable.ic_launcher_back_light_foreground : R.drawable.ic_launcher_back_foreground);
        getSupportActionBar().setHomeAsUpIndicator(IconResId);

        inputVoterId = findViewById(R.id.editVoterId);
        submitButton = findViewById(R.id.sumbitButton);
        cancelButton = findViewById(R.id.onCancelButtonClick);
        updateButton = findViewById(R.id.onUpdateButtonClick);
        resultView = findViewById(R.id.resultViewLayout);

        viewName = findViewById(R.id.voterName);
        viewVoterId = findViewById(R.id.voterId);
        viewFatherName = findViewById(R.id.setFatherName);
        viewGender = findViewById(R.id.setGender);
        viewDob = findViewById(R.id.setDateOfBirth);
        viewAge = findViewById(R.id.setAge);
        viewPhoneNo = findViewById(R.id.setPhoneNumber);
        viewAadhaarNo = findViewById(R.id.setAadhaarNumber);
        viewAddress = findViewById(R.id.setAddress);
        viewState = findViewById(R.id.setState);
        viewDistrict = findViewById(R.id.setDistrict);
        viewAssembly = findViewById(R.id.setAssembly);
        viewVoterImage = findViewById(R.id.voterImageView);

        cancelButton.setOnClickListener(view -> {
            inputVoterId.setText("");
            resultView.setVisibility(View.INVISIBLE);
            resetPage();
        });

        updateButton.setOnClickListener(view -> {

            if(!Objects.equals(voterName, viewName.getText().toString()) || !Objects.equals(voterDob, viewDob.getText().toString())){
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

                try {
                    Date selectedDate = simpleDateFormat.parse(viewDob.getText().toString());
                    Date currentDate = simpleDateFormat.parse(simpleDateFormat.format(new Date()));
                    Objects.requireNonNull(currentDate);

                    if(currentDate.after(selectedDate)){
                        Toast.makeText(getApplicationContext(), "Date OK", Toast.LENGTH_SHORT).show();
                        updateUser();
                    } else Toast.makeText(getApplicationContext(), "Date Error : Future date is selected", Toast.LENGTH_SHORT).show();

                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }else Toast.makeText(getApplicationContext(), "update unchanged", Toast.LENGTH_SHORT).show();
        });
    }


    public boolean isDarkTheme(Context context) {
        int nightMode = context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        return nightMode == Configuration.UI_MODE_NIGHT_YES;
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

    public void onDatePicker(View view) {
        MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
        MaterialDatePicker<Long> materialDatePicker = builder.build();
        materialDatePicker.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
        materialDatePicker.addOnPositiveButtonClickListener(selection -> {
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            calendar.setTimeInMillis(selection);

            @SuppressLint("SimpleDateFormat")
            String format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.getTime());
            //String formattedDate = format.format(calendar.getTime());
            viewDob.setText(format);
        });
    }

    public void onSearchClick(View view) {

        String voterId = Objects.requireNonNull(inputVoterId.getText()).toString();
        
        if(!voterId.isEmpty()){
            RetrofitService retrofitService = new RetrofitService();

            PersonApi personApi = retrofitService.getRetrofit().create(PersonApi.class);
            StatesApi statesApi = retrofitService.getRetrofit().create(StatesApi.class);
            DistrictApi districtApi = retrofitService.getRetrofit().create(DistrictApi.class);
            AssemblyApi assemblyApi = retrofitService.getRetrofit().create(AssemblyApi.class);
            personApi.getPersonByEpic(voterId).enqueue(new Callback<Person>() {
                @Override
                public void onResponse(@NonNull Call<Person> call, @NonNull Response<Person> response) {
                    if(response.isSuccessful()){
                        person = response.body();

                        resultView.setVisibility(View.VISIBLE);
                        getPersonImage(person.getImageId().getId());
                        viewName.setText(Objects.requireNonNull(person).getName());
                        viewVoterId.setText(person.getEpicNumber());
                        viewFatherName.setText(person.getFatherName());
                        viewGender.setText(person.getGender());
                        viewDob.setText(person.getDateOfBirth());
                        viewAge.setText(String.valueOf(person.getAge()));
                        viewAadhaarNo.setText(person.getAadhaarNumber());
                        viewPhoneNo.setText(person.getPhoneNumber());
                        viewAddress.setText(person.getAddress());

                        // copy the person original name and dateOfBirth
                        voterName = person.getName();
                        voterDob = person.getDateOfBirth();

                        statesApi.getStatesById(person.getStateId()).enqueue(new Callback<States>() {
                            @Override
                            public void onResponse(@NonNull Call<States> call, @NonNull Response<States> response) {
                                if(response.isSuccessful()){
                                    states = response.body();
                                    viewState.setText(Objects.requireNonNull(states).getState());
                                }
                            }
                            @Override
                            public void onFailure(@NonNull Call<States> call, @NonNull Throwable t) {

                            }
                        });
                        districtApi.getDistrictById(person.getDistrictId()).enqueue(new Callback<District>() {
                            @Override
                            public void onResponse(@NonNull Call<District> call, @NonNull Response<District> response) {
                                if(response.isSuccessful()) {
                                    district = response.body();
                                    viewDistrict.setText(Objects.requireNonNull(district).getDistrict());
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<District> call, @NonNull Throwable t) {

                            }
                        });
                        assemblyApi.getById(person.getAssemblyId()).enqueue(new Callback<Assembly>() {
                            @Override
                            public void onResponse(@NonNull Call<Assembly> call, @NonNull Response<Assembly> response) {
                                if(response.isSuccessful()){
                                    assembly = response.body();
                                    viewAssembly.setText(Objects.requireNonNull(assembly).getAssembly());
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<Assembly> call, @NonNull Throwable t) {

                            }
                        });
                    }
                }
                @Override
                public void onFailure(@NonNull Call<Person> call, @NonNull Throwable t) {

                }
            });
        } else Toast.makeText(getApplicationContext(), "voter id is empty", Toast.LENGTH_SHORT).show();
        
    }

    public void updateUser() {
        RetrofitService retrofitService = new RetrofitService();
        PersonApi personApi = retrofitService.getRetrofit().create(PersonApi.class);

        person.setName(viewName.getText().toString());
        person.setDateOfBirth(viewDob.getText().toString());
        personApi.updatePersonById(person.getId(), person).enqueue(new Callback<Person>() {
            @Override
            public void onResponse(@NonNull Call<Person> call, @NonNull Response<Person> response) {
                if(response.isSuccessful()) {
                    Objects.requireNonNull(response.body());
                    Toast.makeText(UpdateActivity.this, "update success : "+response.body().getName(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(@NonNull Call<Person> call, @NonNull Throwable t) {
                Toast.makeText(getApplicationContext(), "update user failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getPersonImage(long id) {
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
                    Bitmap userImageBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                    viewVoterImage.setImageBitmap(userImageBitmap);

                }
            }
            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Toast.makeText(getApplicationContext(), "Image fetch failed", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void resetPage() {
        viewName.setText("");
        viewVoterId.setText("");
        viewFatherName.setText("");
        viewGender.setText("");
        viewDob.setText("");
        viewAge.setText("");
        viewPhoneNo.setText("");
        viewAadhaarNo.setText("");
        viewAddress.setText("");
        viewState.setText("");
        viewDistrict.setText("");
        viewAssembly.setText("");
    }
}

