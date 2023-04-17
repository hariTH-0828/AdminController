package edu.mobile.voterlist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Objects;

public class SearchResultActivity extends AppCompatActivity {

    TextView name, fatherName, sex, date_of_birth, age, phoneNo, aadhaarNo, state, district, assembly, epicNo, address;
    ImageView userProfilePhoto;
    Bitmap userImageBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        int IconResId = (isDarkTheme(getApplicationContext()) ? R.drawable.ic_launcher_back_light_foreground : R.drawable.ic_launcher_back_foreground);
        getSupportActionBar().setHomeAsUpIndicator(IconResId);

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
        address = findViewById(R.id.voterAddress);
        userProfilePhoto = findViewById(R.id.user_image);

        displayResult();
    }

    private void displayResult() {
        Intent intent = getIntent();
        ArrayList<String> personList = intent.getStringArrayListExtra("personList");

        String stateName = intent.getStringExtra("stateName");
        String districtName = intent.getStringExtra("districtName");
        String assemblyName = intent.getStringExtra("assemblyName");
        if(intent.getByteArrayExtra("bitmap") != null ){
            byte[] imageBitmap = intent.getByteArrayExtra("bitmap");
            userImageBitmap = BitmapFactory.decodeByteArray(imageBitmap, 0, imageBitmap.length);
            userProfilePhoto.setImageBitmap(userImageBitmap);
        }

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
        address.setText(personList.get(8));
        epicNo.setText(personList.get(7));
    }

    public boolean isDarkTheme(Context context) {
        int nightMode = context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        return nightMode == Configuration.UI_MODE_NIGHT_YES;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}