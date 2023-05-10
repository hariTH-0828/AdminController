package edu.mobile.voterlist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.Objects;

public class HomeScreen extends AppCompatActivity {

    FrameLayout createVoter, updateVoter, listVoter, createCandidate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        createVoter = findViewById(R.id.onCreateVoter);
        updateVoter = findViewById(R.id.onUpdateVoter);
        listVoter = findViewById(R.id.onListVoter);
        createCandidate = findViewById(R.id.onCreateCandidate);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            if(!isDarkTheme(getApplicationContext())){
                actionBar.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.white)));
            }
        }


        createVoter.setOnClickListener(v -> {
            Intent switchView = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(switchView);
        });

        updateVoter.setOnClickListener(v -> {
            Intent switchView = new Intent(getApplicationContext(), UpdateActivity.class);
            startActivity(switchView);
        });

        listVoter.setOnClickListener(v -> {
            Intent switchView = new Intent(getApplicationContext(), ListActivity.class);
            startActivity(switchView);
        });

        createCandidate.setOnClickListener(v -> {
            Intent switchView = new Intent(getApplicationContext(), CreateCandidates.class);
            startActivity(switchView);
        });
    }

    public boolean isDarkTheme(Context context) {
        int nightMode = context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        return nightMode == Configuration.UI_MODE_NIGHT_YES;
    }
}