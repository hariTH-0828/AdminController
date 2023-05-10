package edu.mobile.voterlist;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

public class CreateCandidates extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_candidates);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            if(!isDarkTheme(getApplicationContext())){
                actionBar.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.white)));
            }
        }
    }

    public boolean isDarkTheme(Context context) {
        int nightMode = context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        return nightMode == Configuration.UI_MODE_NIGHT_YES;
    }
}