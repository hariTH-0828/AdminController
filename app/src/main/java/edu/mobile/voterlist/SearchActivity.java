package edu.mobile.voterlist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import edu.mobile.voterlist.retrofit.RetrofitService;

public class SearchActivity extends AppCompatActivity {

    RadioButton byEpic, byAadhaar;
    TextInputLayout layoutAadhaar, layoutEpic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        byEpic = findViewById(R.id.searchByEpic);
        byAadhaar = findViewById(R.id.searchByAadhaar);
        layoutAadhaar = findViewById(R.id.layoutAadhaar);
        layoutEpic = findViewById(R.id.layoutEpic);
    }

    public void onSearchByClick(View view) {

        byEpic.setOnClickListener(v -> {
            byEpic.setChecked(true);
            byAadhaar.setChecked(false);

            layoutEpic.setVisibility(View.VISIBLE);
            layoutAadhaar.setVisibility(View.INVISIBLE);
        });

        byAadhaar.setOnClickListener(v -> {
            byAadhaar.setChecked(true);
            byEpic.setChecked(false);

            layoutEpic.setVisibility(View.INVISIBLE);
            layoutAadhaar.setVisibility(View.VISIBLE);

        });
        byEpic.setChecked(false);
        layoutEpic.setVisibility(View.INVISIBLE);
        layoutAadhaar.setVisibility(View.VISIBLE);
    }

    public void onBackClicked(View view) {
        Intent switchView = new Intent(getApplicationContext(), HomeScreen.class);
        startActivity(switchView);
    }

    public void onAadhaarClick(View view) {
    }

    public void onEpicClick(View view) {
    }
}