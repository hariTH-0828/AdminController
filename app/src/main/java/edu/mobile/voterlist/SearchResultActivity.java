package edu.mobile.voterlist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SearchResultActivity extends AppCompatActivity {

    TextView name, fatherName, sex, date_of_birth, age, phoneNo, aadhaarNo, state, district, assembly, epicNo;
    Button doneButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        name = findViewById(R.id.voterName);
        fatherName = findViewById(R.id.voterFatherName);
        sex = findViewById(R.id.voterSex);
        date_of_birth = findViewById(R.id.voterDateOfBirth);
        age = findViewById(R.id.voterAge);
        phoneNo = findViewById(R.id.voterPhone);
        aadhaarNo = findViewById(R.id.voterAadhaar);
        state = findViewById(R.id.voterState);
        district = findViewById(R.id.voterDistrict);
        assembly = findViewById(R.id.voterAssembly);
        epicNo = findViewById(R.id.VoterEpicNumber);
        doneButton = findViewById(R.id.done_button);

        displayResult();


        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), HomeScreen.class));
            }
        });
    }

    private void displayResult() {
        Intent intent = getIntent();
        ArrayList<String> personList = intent.getStringArrayListExtra("personList");
        String stateName = intent.getStringExtra("stateName");
        String districtName = intent.getStringExtra("districtName");
        String assemblyName = intent.getStringExtra("assemblyName");

        if(!stateName.isEmpty()) {
            state.setText(stateName);
        } else Toast.makeText(getApplicationContext(), "state is null", Toast.LENGTH_SHORT).show();
        
        if(!districtName.isEmpty()){
            district.setText(districtName);
        } else Toast.makeText(this, "district is null", Toast.LENGTH_SHORT).show();
        
        if(!assemblyName.isEmpty()) {
            assembly.setText(assemblyName);
        } else Toast.makeText(this, "assembly is null", Toast.LENGTH_SHORT).show();

        name.setText(personList.get(0));
        fatherName.setText(personList.get(1));
        sex.setText(personList.get(2));
        date_of_birth.setText(personList.get(3));
        age.setText(personList.get(4));
        phoneNo.setText(personList.get(5));
        aadhaarNo.setText(personList.get(6));
        epicNo.setText(personList.get(7));
    }
}