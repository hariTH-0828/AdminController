package edu.mobile.voterlist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

public class SearchResultActivity extends AppCompatActivity {

    TextView name, fatherName, sex, date_of_birth, age, phoneNo, aadhaarNo, state, district, assembly, epicNo;

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

        displayResult();
    }

    private void displayResult() {
        Intent intent = getIntent();
        ArrayList<String> personList = intent.getStringArrayListExtra("PersonList");

        name.setText(personList.get(0));

    }
}