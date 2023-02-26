package edu.mobile.voterlist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


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

        // Dropdown -> States
        reference = FirebaseDatabase.getInstance().getReference("states");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> stateList = new ArrayList<>();
                for(DataSnapshot childNode : snapshot.getChildren()){
                    String value = childNode.getValue(String.class);
                    stateList.add(value);
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.dropdown_list, stateList);
                autoCompleteStateView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Crash", Toast.LENGTH_SHORT).show();
            }
        });

        // Dropdown -> District
        autoCompleteDistrictView.setOnClickListener(view -> {
            state = autoCompleteStateView.getText().toString();
            if(!state.isEmpty()) {

                DatabaseReference districtRef = FirebaseDatabase.getInstance().getReference("District").child(state);
                districtRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ArrayList<String> districtList = new ArrayList<>();
                        for (DataSnapshot childNode : snapshot.getChildren()) {
                            String value = childNode.getValue(String.class);
                            districtList.add(value);
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.dropdown_list, districtList);
                        autoCompleteDistrictView.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getApplicationContext(), "Crash", Toast.LENGTH_SHORT).show();
                    }
                });
            }else{
                Toast.makeText(getApplicationContext(), "select state", Toast.LENGTH_SHORT).show();
            }
        });

        // Dropdown -> Assembly
        autoCompleteAssemblyView.setOnClickListener(view -> {
            district = autoCompleteDistrictView.getText().toString();
            if(!district.isEmpty()){

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                reference = database.getReference("Assembly/"+district);

                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ArrayList<String> assemblyList = new ArrayList<>();
                        for(DataSnapshot childNode : snapshot.getChildren()){
                            String value = childNode.getValue(String.class);
                            assemblyList.add(value);
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.dropdown_list, assemblyList);
                        autoCompleteAssemblyView.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getApplicationContext(), "Crash", Toast.LENGTH_SHORT).show();
                    }
                });
            }else Toast.makeText(getApplicationContext(), "Select District", Toast.LENGTH_SHORT).show();
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
//        resetPage();
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