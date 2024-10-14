package com.example.indegenousmedicine2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class DrugInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_drug_info);
        // Apply language when activity is created
        LanguageManager.applyLanguage(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Apply language when activity is resumed
        LanguageManager.applyLanguage(this);
    }
}