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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    AutoCompleteTextView autoCompleteTextView;
    String name, fatherName, phoneNo, state, assembly, wardNo, epicNo;
    EditText editName, editFatherName, editPhone, editAssembly, editWard, editEpic;
    String getGender;
    RadioGroup radioGroup;
    RadioButton genderBtn;

    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editName = findViewById(R.id.editName);
        editFatherName = findViewById(R.id.editFatherName);
        editPhone = findViewById(R.id.editPhone);
        editAssembly = findViewById(R.id.editAssembly);
        editWard = findViewById(R.id.editWard);
        editEpic = findViewById(R.id.editEpic);
        radioGroup = findViewById(R.id.groupRadio);
        autoCompleteTextView = findViewById(R.id.editState);

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
                autoCompleteTextView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Crash", Toast.LENGTH_SHORT).show();
            }
        });

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

    public void getUser(View view) {
        state = autoCompleteTextView.getText().toString();
        readData(state);
    }

    private void readData(String state) {
        reference = FirebaseDatabase.getInstance().getReference("District");
        reference.child(state).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful() && task.getResult().exists()){
                    DataSnapshot snapshot = task.getResult();
                    String district = String.valueOf(snapshot.child("0").getValue());

                    Toast.makeText(getApplicationContext(), "District is "+district, Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(), "Failed to read", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}