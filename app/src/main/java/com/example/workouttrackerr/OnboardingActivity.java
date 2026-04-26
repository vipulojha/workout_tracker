package com.example.workouttrackerr;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;

public class OnboardingActivity extends AppCompatActivity {
    private EditText etWeight, etHeight;
    private Spinner spinnerGoal, spinnerFocus, spinnerLevel;
    private AppDataManager dataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new AppSettings(this).applyTheme();
        setContentView(R.layout.activity_onboarding);

        dataManager = new AppDataManager(this);
        etWeight = findViewById(R.id.etWeight);
        etHeight = findViewById(R.id.etHeight);
        spinnerGoal = findViewById(R.id.spinnerGoal);
        spinnerFocus = findViewById(R.id.spinnerFocus);
        spinnerLevel = findViewById(R.id.spinnerLevel);
        Button btnFinish = findViewById(R.id.btnCompleteOnboarding);

        String[] goals = {"Lose weight", "Build muscle (Bulk)", "Lean muscle", "Improve endurance", "Maintain fitness"};
        spinnerGoal.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, goals));

        String[] focusAreas = {"Full body", "Upper body", "Lower body", "Core / Abs", "Specific muscle group"};
        spinnerFocus.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, focusAreas));

        String[] levels = {"Beginner (0-6 months)", "Intermediate (6-24 months)", "Advanced (2+ years)", "Athlete"};
        spinnerLevel.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, levels));

        btnFinish.setOnClickListener(v -> {
            String profile = "Weight: " + etWeight.getText().toString().trim() + " kg"
                    + ", Height: " + etHeight.getText().toString().trim() + " cm"
                    + ", Goal: " + spinnerGoal.getSelectedItem()
                    + ", Focus: " + spinnerFocus.getSelectedItem()
                    + ", Level: " + spinnerLevel.getSelectedItem();
            dataManager.saveBodyProfile(profile);
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
    }
}
