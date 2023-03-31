package edu.mobile.voterlist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

import com.google.android.material.textfield.TextInputEditText;

import edu.mobile.voterlist.retrofit.RetrofitService;

public class SearchActivity extends AppCompatActivity {

    RadioButton byEpic, byAadhaar;
    TextInputEditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        byEpic = findViewById(R.id.searchByEpic);
        byAadhaar = findViewById(R.id.searchByAadhaar);
        editText = findViewById(R.id.editInputText);
    }

    public void onSearchByClick(View view) {

        byEpic.setOnClickListener(v -> {
            byEpic.setChecked(true);
            byAadhaar.setChecked(false);
        });

        byAadhaar.setOnClickListener(v -> {
            byAadhaar.setChecked(true);
            byEpic.setChecked(false);

        });
        byEpic.setChecked(false);
    }

    public void onBackClicked(View view) {
        Intent switchView = new Intent(getApplicationContext(), HomeScreen.class);
        startActivity(switchView);
    }

    public void onSearchClick(View view) {
        RetrofitService retrofitService = new RetrofitService();

    }
}