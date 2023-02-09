package edu.mobile.voterlist;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;


public class MainActivity extends AppCompatActivity {

    // Initialize database variables
    String name, fatherName, voterId, phoneNo, state, assembly, wardNo, epicNo;

    // Initialize Firebase objects


    EditText editName, editFatherName, editVoterID, editPhone, editState, editAssembly, editWard, editEpic;
    String getGender;
    RadioGroup radioGroup;
    RadioButton genderBtn;
    Button pushBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editName = findViewById(R.id.editName);
        editFatherName = findViewById(R.id.editFatherName);
        editVoterID = findViewById(R.id.editVoterID);
        editPhone = findViewById(R.id.editPhone);
        editState = findViewById(R.id.editState);
        editAssembly = findViewById(R.id.editAssembly);
        editWard = findViewById(R.id.editWard);
        editEpic = findViewById(R.id.editEpic);
        radioGroup = findViewById(R.id.groupradio);
        pushBtn = findViewById(R.id.pushBtn);
    }

    public void onRadioButtonClicked(View view) {
        int selectedId = radioGroup.getCheckedRadioButtonId();
        genderBtn = findViewById(selectedId);

        getGender = genderBtn.toString();
        Log.d("Gender", getGender);
    }

    public void onPushClick(View view) {
        name = editName.getText().toString().trim();
        fatherName = editFatherName.getText().toString().trim();
        voterId = editVoterID.getText().toString();
        phoneNo = editPhone.getText().toString();
        state = editState.getText().toString();
        assembly = editAssembly.getText().toString();
        wardNo = editWard.getText().toString();
        epicNo = editEpic.getText().toString();

        Log.d("Button_status", "Push clicked");

        if(!name.isEmpty() && !fatherName.isEmpty() && !voterId.isEmpty() && !phoneNo.isEmpty() && !getGender.isEmpty()
                && !state.isEmpty() && !assembly.isEmpty() && !wardNo.isEmpty() && !epicNo.isEmpty()) {

            Log.d("Status", name+" "+fatherName+" "+voterId);
        }

    }


}