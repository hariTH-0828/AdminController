package edu.mobile.voterlist;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class HomeScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
    }

    public void onCreateVoter(View view) {
        Intent swithView = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(swithView);
    }

    public void onSearchVoter(View view) {
        Intent switchView = new Intent(getApplicationContext(), SearchActivity.class);
        startActivity(switchView);
    }

    public void onUpdateVoter(View view) {
        Intent switchView = new Intent(getApplicationContext(), UpdateActivity.class);
        startActivity(switchView);
    }
}