package edu.mobile.voterlist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

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
    String name, fatherName, phoneNo, state, district, age, aadhaar_number, dateOfBirth, getGender;
    int stateId;
    EditText editName, editFatherName, editPhone, editWard, editEpic, editDob, editAadhaar, editAge;
    TextInputLayout datePicker;
    Button saveBtn;
    RadioGroup radioGroup;
    RadioButton genderBtn;

    MaterialDatePicker.Builder<? extends Long> materialDateBuilder = MaterialDatePicker.Builder.datePicker();
    final MaterialDatePicker<? extends Object> materialDatePicker = materialDateBuilder.build();

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
        saveBtn = findViewById(R.id.pushBtn);

        loadStates();
    }

    private void loadStates() {
        RetrofitService retrofitService = new RetrofitService();
        StatesApi statesApi= retrofitService.getRetrofit().create(StatesApi.class);

        statesApi.getAllStates()
                .enqueue(new Callback<List<States>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<States>> call, @NonNull Response<List<States>> response) {
                        List<States> states = response.body();
                        List<String> statesList = new ArrayList<>();

                        assert states != null;
                        for(States item : states){
                            statesList.add(item.getState());
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.dropdown_list, statesList);
                        autoCompleteStateView.setAdapter(adapter);
                    }
                    @Override
                    public void onFailure(@NonNull Call<List<States>> call, @NonNull Throwable t) {
                        Toast.makeText(MainActivity.this, "states load failed..", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // RadioButton -> Gender
    public void onRadioButtonClicked(View view) {
        int selectedId = radioGroup.getCheckedRadioButtonId();
        genderBtn = findViewById(selectedId);
        getGender = genderBtn.getText().toString();
        Toast.makeText(getApplicationContext(), getGender, Toast.LENGTH_SHORT).show();
    }

   public void onPushClick(View view) {
       state = autoCompleteStateView.getText().toString();
       getStateIdByName(state);
   }

    private void savePerson(int sId) {
        RetrofitService retrofitService = new RetrofitService();
        PersonApi personApi = retrofitService.getRetrofit().create(PersonApi.class);

        name = editName.getText().toString().trim();
        fatherName = editFatherName.getText().toString().trim();
        phoneNo = editPhone.getText().toString();
        age = editAge.getText().toString();
        aadhaar_number = editAadhaar.getText().toString();
        stateId = sId;


        // Creating Person Object
        Person person = new Person();

        person.setName(name);
        person.setFatherName(fatherName);
        person.setAge(Integer.parseInt(age));
        person.setPhoneNumber(phoneNo);
        person.setAadhaarNumber(aadhaar_number);
        person.setStateId(stateId);
        person.setGender(getGender);

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
    }

    private void getStateIdByName(String stateName){
        RetrofitService retrofitService = new RetrofitService();
        StatesApi statesApi = retrofitService.getRetrofit().create(StatesApi.class);
        Call<Integer> id = statesApi.getStateIdByName(stateName);
        id.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(@NonNull Call<Integer> call, @NonNull Response<Integer> response) {
                if(response.isSuccessful()){
                    Integer id = response.body();
                    postStateId(id);
                }
            }
            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Toast.makeText(MainActivity.this, "State id fetch failed...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void postStateId(int id){
        savePerson(id);
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

    public void onDatePicker(View view) {
        materialDatePicker.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
        materialDatePicker.addOnPositiveButtonClickListener((MaterialPickerOnPositiveButtonClickListener<? super Object>) selection -> editDob.setText(materialDatePicker.getHeaderText()));
    }


}