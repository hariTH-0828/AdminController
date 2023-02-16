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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.nio.charset.StandardCharsets;


public class MainActivity extends AppCompatActivity {

    AutoCompleteTextView autoCompleteTextView;
    String name, fatherName, phoneNo, state, assembly, wardNo, epicNo;
    EditText editName, editFatherName, editPhone, editAssembly, editWard, editEpic;
    String getGender;
    RadioGroup radioGroup;
    RadioButton genderBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        autoCompleteTextView = findViewById(R.id.editState);

        editName = findViewById(R.id.editName);
        editFatherName = findViewById(R.id.editFatherName);
        editPhone = findViewById(R.id.editPhone);
        editAssembly = findViewById(R.id.editAssembly);
        editWard = findViewById(R.id.editWard);
        editEpic = findViewById(R.id.editEpic);
        radioGroup = findViewById(R.id.groupRadio);

        String[] state = new String[]{
            "Andhra Pradesh","Arunachal Pradesh", "Assam", "Bihar", "Chhattisgarh", "Delhi", "Goa",
            "Gujarat", "Haryana", "Himachal Pradesh", "Jammu and Kashmir", "Jharkhand", "Karnataka", "Kerala", "Madhya Pradesh",
            "Maharashtra", "Manipur", "Meghalaya", "Mizoram", "Nagaland", "Odisha", "Punjab", "Puducherry", "Rajasthan",
            "Sikkim", "Tamil Nadu", "Telangana", "Tripura", "Uttarakhand", "Uttar Pradesh", "West Bengal"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.dropdown_list, state);
        autoCompleteTextView.setAdapter(adapter);

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
        phoneNo = editPhone.getText().toString();
        state = autoCompleteTextView.getText().toString();
        assembly = editAssembly.getText().toString();
        wardNo = editWard.getText().toString();
        epicNo = editEpic.getText().toString();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Users");

        if(!name.isEmpty() && !fatherName.isEmpty() && !phoneNo.isEmpty() && !state.isEmpty() && !assembly.isEmpty()
            && !wardNo.isEmpty() && !epicNo.isEmpty() && !getGender.isEmpty()){

            Users user = new Users(name, fatherName, getGender, phoneNo, state, assembly, wardNo, epicNo);
            reference.child(name).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(getApplicationContext(), "Add user successfully", Toast.LENGTH_LONG).show();
                }
            });

            editName.setText("");
            editFatherName.setText("");
            editPhone.setText("");
            editAssembly.setText("");
            editWard.setText("");
            editEpic.setText("");
        }else{
            Toast.makeText(getApplicationContext(), "Push Failed!", Toast.LENGTH_SHORT).show();
        }
    }
}