package edu.mobile.voterlist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import edu.mobile.voterlist.api.DistrictApi;
import edu.mobile.voterlist.model.District;
import edu.mobile.voterlist.model.Person;
import edu.mobile.voterlist.model.States;
import edu.mobile.voterlist.api.PersonApi;
import edu.mobile.voterlist.retrofit.RetrofitService;
import edu.mobile.voterlist.api.StatesApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    AutoCompleteTextView autoCompleteStateView, autoCompleteDistrictView, autoCompleteAssemblyView;
    String name, fatherName, phoneNo, aadhaar_number, getGender, dateOfBirth, age;
    int stateId, districtId;
    EditText editName, editFatherName, editPhone, editWard, editEpic, editDob, editAadhaar, editAge;
    TextInputLayout datePicker;
    RadioGroup radioGroup;
    RadioButton genderBtn;
    MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
    MaterialDatePicker<Long> materialDatePicker = builder.build();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editName = findViewById(R.id.editName);
        editFatherName = findViewById(R.id.editFatherName);
        editPhone = findViewById(R.id.editPhone);
        editWard = findViewById(R.id.editWard);
        editEpic = findViewById(R.id.editEpic);
        editAadhaar = findViewById(R.id.editAadhaar);
        radioGroup = findViewById(R.id.groupRadio);
        editDob = findViewById(R.id.editDateOfBirth);
        editAge = findViewById(R.id.editAge);
        autoCompleteStateView = findViewById(R.id.editState);
        autoCompleteDistrictView = findViewById(R.id.editDistrict);
        autoCompleteAssemblyView = findViewById(R.id.editAssembly);
        datePicker = findViewById(R.id.textLayoutDate);

        datePicker.setEndIconOnClickListener(this::onDatePicker);

        loadStates();

        autoCompleteStateView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                States sId = (States) parent.getItemAtPosition(position);
                stateId = sId.getId();
                loadDistrict(sId.getId());
            }
        });

        autoCompleteDistrictView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                District dId = (District) adapterView.getItemAtPosition(i);
                districtId = dId.getId();
            }
        });
    }

    public void onRadioButtonClicked(View view) {
        int selectedId = radioGroup.getCheckedRadioButtonId();
        genderBtn = findViewById(selectedId);
        getGender = genderBtn.getText().toString();
        Toast.makeText(getApplicationContext(), getGender, Toast.LENGTH_SHORT).show();
    }
    public void onDatePicker(View view) {
        materialDatePicker.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
        materialDatePicker.addOnPositiveButtonClickListener(selection -> {
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            calendar.setTimeInMillis(selection);
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = format.format(calendar.getTime());
            editDob.setText(formattedDate);
        });
    }
    private void loadStates() {
        RetrofitService retrofitService = new RetrofitService();
        StatesApi statesApi= retrofitService.getRetrofit().create(StatesApi.class);

        statesApi.getAllStates()
                .enqueue(new Callback<List<States>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<States>> call, @NonNull Response<List<States>> response) {
                        List<States> states = response.body();
                        ArrayAdapter<States> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.dropdown_list, states);
                        autoCompleteStateView.setAdapter(adapter);
                    }
                    @Override
                    public void onFailure(@NonNull Call<List<States>> call, @NonNull Throwable t) {
                        Toast.makeText(MainActivity.this, "states load failed..", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void loadDistrict(int stateId) {
        RetrofitService retrofitService = new RetrofitService();
        DistrictApi districtApi = retrofitService.getRetrofit().create(DistrictApi.class);

        districtApi.getDistrictByStateId(stateId)
                .enqueue(new Callback<List<District>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<District>> call,@NonNull Response<List<District>> response) {
                        List<District> districtList = response.body();
                        ArrayAdapter<District> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.dropdown_list, districtList);
                        autoCompleteDistrictView.setAdapter(adapter);
                    }

                    @Override
                    public void onFailure(Call<List<District>> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "District fetch failed..", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void savePerson() {
        RetrofitService retrofitService = new RetrofitService();
        PersonApi personApi = retrofitService.getRetrofit().create(PersonApi.class);


        personApi.isExist(aadhaar_number)
                .enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(@NonNull Call<Boolean> call,@NonNull Response<Boolean> response) {
                        boolean flag = Boolean.TRUE.equals(response.body());
                        if(!flag) {
                            Person person = new Person();

                            person.setName(name);
                            person.setGender(getGender);
                            person.setDateOfBirth(dateOfBirth);
                            person.setAge(Integer.parseInt(age));
                            person.setFatherName(fatherName);
                            person.setPhoneNumber(phoneNo);
                            person.setAadhaarNumber(aadhaar_number);
                            person.setStateId(stateId);
                            person.setDistrictId(districtId);

                            personApi.save(person)
                                    .enqueue(new Callback<Person>() {
                                        @Override
                                        public void onResponse(@NonNull Call<Person> call, @NonNull Response<Person> response) {
                                            Toast.makeText(MainActivity.this, "save person successful...", Toast.LENGTH_SHORT).show();
                                        }
                                        @Override
                                        public void onFailure(@NonNull Call<Person> call, @NonNull Throwable t) {
                                            Toast.makeText(MainActivity.this, "save person failed...", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            Toast.makeText(MainActivity.this, "You've registered already.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "Aadhaar fetch failed....", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void resetPage() {
        editName.setText("");
        editDob.setText("");
        editAge.setText("");
        editFatherName.setText("");
        editPhone.setText("");
        editWard.setText("");
        editEpic.setText("");
        editAadhaar.setText("");
        autoCompleteStateView.setText("");
        autoCompleteStateView.clearFocus();
        autoCompleteDistrictView.setText("");
        autoCompleteDistrictView.clearFocus();
        autoCompleteAssemblyView.setText("");
        autoCompleteAssemblyView.clearFocus();
    }


    public void validateForm(View view) {
        name = editName.getText().toString().trim();
        dateOfBirth = editDob.getText().toString();
        fatherName = editFatherName.getText().toString().trim();
        phoneNo = editPhone.getText().toString();
        age = editAge.getText().toString();
        aadhaar_number = editAadhaar.getText().toString();

        if(!name.isEmpty() && !getGender.isEmpty() && !dateOfBirth.isEmpty() && !age.isEmpty() && !fatherName.isEmpty()
                && !phoneNo.isEmpty() && !aadhaar_number.isEmpty() && stateId != 0 && districtId != 0){
            savePerson();
        } else Toast.makeText(getApplicationContext(), "Please fill all the fields", Toast.LENGTH_SHORT).show();
    }
}