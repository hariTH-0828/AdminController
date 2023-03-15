package edu.mobile.voterlist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.mobile.voterlist.model.States;
import edu.mobile.voterlist.retrofit.RetrofitService;
import edu.mobile.voterlist.retrofit.StatesApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    AutoCompleteTextView autoCompleteStateView, autoCompleteDistrictView, autoCompleteAssemblyView;
    String name, fatherName, phoneNo, state, district, wardNo, epicNo, dateOfBirth;
    EditText editName, editFatherName, editPhone, editWard, editEpic, editDob;
    TextInputLayout datePicker;
    String getGender;
    RadioGroup radioGroup;
    RadioButton genderBtn;
    DatabaseReference reference;


    MaterialDatePicker.Builder<? extends Long> materialDateBuilder = MaterialDatePicker.Builder.datePicker();
    final MaterialDatePicker<? extends Object> materialDatePicker = materialDateBuilder.build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadStates();

        editName = findViewById(R.id.editName);
        editFatherName = findViewById(R.id.editFatherName);
        editPhone = findViewById(R.id.editPhone);
        editWard = findViewById(R.id.editWard);
        editEpic = findViewById(R.id.editEpic);
        radioGroup = findViewById(R.id.groupRadio);
        editDob = findViewById(R.id.editDateOfBirth);
        autoCompleteStateView = findViewById(R.id.editState);
        autoCompleteDistrictView = findViewById(R.id.editDistrict);
        autoCompleteAssemblyView = findViewById(R.id.editAssembly);
        datePicker = findViewById(R.id.textLayoutDate);

        datePicker.setEndIconOnClickListener(this::onDatePicker);
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
                        Toast.makeText(MainActivity.this, "Load Complete..", Toast.LENGTH_SHORT).show();
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.dropdown_list, statesList);
                        autoCompleteStateView.setAdapter(adapter);
                    }
                    @Override
                    public void onFailure(@NonNull Call<List<States>> call, @NonNull Throwable t) {
                        Toast.makeText(MainActivity.this, "load failed..", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // RadioButton -> Gender
    public void onRadioButtonClicked(View view) {
        int selectedId = radioGroup.getCheckedRadioButtonId();
        genderBtn = findViewById(selectedId);
        getGender = genderBtn.getText().toString();
        Log.d("Gender", getGender);
    }

    // Create user profiles
    public void onPushClick(View view) {
        saveData();
        resetPage();
    }

    private void saveData() {
        name = editName.getText().toString().trim();
        dateOfBirth = editDob.getText().toString().trim();
        fatherName = editFatherName.getText().toString().trim();
        phoneNo = editPhone.getText().toString();
        state = autoCompleteStateView.getText().toString();
        district = autoCompleteDistrictView.getText().toString();
        wardNo = editWard.getText().toString();
        epicNo = editEpic.getText().toString();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Users");

        if(!name.isEmpty() && !dateOfBirth.isEmpty() && !fatherName.isEmpty() && !phoneNo.isEmpty() && !state.isEmpty() && !district.isEmpty()
            && !wardNo.isEmpty() && !epicNo.isEmpty() && !getGender.isEmpty()){



            Users user = new Users(name, dateOfBirth, fatherName, getGender, phoneNo, state, district, wardNo, epicNo);
            reference.child(name).setValue(user).addOnCompleteListener(task -> Toast.makeText(getApplicationContext(), "Add user successfully", Toast.LENGTH_LONG).show());
            resetPage();

        }else{
            Toast.makeText(getApplicationContext(), "Push Failed!", Toast.LENGTH_SHORT).show();
        }
    }

    private void resetPage() {
        editName.setText("");
        editDob.setText("");
        editFatherName.setText("");
        editPhone.setText("");
        editWard.setText("");
        editEpic.setText("");
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