package edu.mobile.voterlist;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    AutoCompleteTextView autoCompleteTextView;
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

        autoCompleteTextView = findViewById(R.id.editState);
        String[] state = new String[]{
           "Andhra Pradesh","Arunachal Pradesh", "Assam", "Bihar", "Chhattisgarh", "Delhi", "Goa",
           "Gujarat", "Haryana", "Himachal Pradesh", "Jammu and Kashmir", "Jharkhand", "Karnataka", "Kerala", "Madhya Pradesh",
           "Maharashtra", "Manipur", "Meghalaya", "Mizoram", "Nagaland", "Odisha", "Punjab", "Puducherry", "Rajasthan",
           "Sikkim", "Tamil Nadu", "Telangana", "Tripura", "Uttarakhand", "Uttar Pradesh", "West Bengal"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.dropdown_list, state);
        autoCompleteTextView.setAdapter(adapter);

        editName = findViewById(R.id.editName);
        editFatherName = findViewById(R.id.editFatherName);
        editVoterID = findViewById(R.id.editVoterID);
        editPhone = findViewById(R.id.editPhone);
        editAssembly = findViewById(R.id.editAssembly);
        editWard = findViewById(R.id.editWard);
        editEpic = findViewById(R.id.editEpic);
        radioGroup = findViewById(R.id.groupRadio);
        pushBtn = findViewById(R.id.pushBtn);

        /*autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(), ""+autoCompleteTextView.getText().toString(), Toast.LENGTH_LONG).show();
            }
        });*/

    }

    public void onRadioButtonClicked(View view) {
        int selectedId = radioGroup.getCheckedRadioButtonId();
        genderBtn = findViewById(selectedId);

        getGender = genderBtn.getText().toString();
        Log.d("Gender", getGender);
    }

    public void onPushClick(View view) {
        name = editName.getText().toString().trim();
        fatherName = editFatherName.getText().toString().trim();
        voterId = editVoterID.getText().toString();
        phoneNo = editPhone.getText().toString();
        state = autoCompleteTextView.getText().toString();
        assembly = editAssembly.getText().toString();
        wardNo = editWard.getText().toString();
        epicNo = editEpic.getText().toString();

        Log.d("Button_status", "Push clicked");

        if(!name.isEmpty() && !fatherName.isEmpty() && !voterId.isEmpty() && !phoneNo.isEmpty() && !getGender.isEmpty()
                && !state.isEmpty() && !assembly.isEmpty() && !wardNo.isEmpty() && !epicNo.isEmpty()) {

            Log.d("Button_status", name+" "+fatherName+" "+voterId+" "+state);
        }

    }


}