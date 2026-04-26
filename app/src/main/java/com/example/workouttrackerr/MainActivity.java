package com.example.workouttrackerr;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNav;
    private AppDataManager dataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new AppSettings(this).applyTheme();
        if (!new AuthManager(this).isLoggedIn()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }
        setContentView(R.layout.activity_main);
        dataManager = new AppDataManager(this);

        bottomNav = findViewById(R.id.bottom_navigation);

        // Load Home screen by default
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new HomeFragment())
                    .commit();
        }

        // Setup menu clicks
        if (bottomNav != null) {
            bottomNav.setOnItemSelectedListener(item -> {
                Fragment selected = null;
                int itemId = item.getItemId();

                if (itemId == R.id.nav_home) {
                    selected = new HomeFragment();
                } else if (itemId == R.id.nav_plans) {
                    selected = new PlansFragment();
                } else if (itemId == R.id.nav_exercises) {
                    selected = new ExercisesFragment();
                } else if (itemId == R.id.nav_performance) {
                    selected = new PerformanceFragment();
                } else if (itemId == R.id.nav_profile) {
                    selected = new ProfileFragment();
                }

                if (selected != null) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, selected)
                            .commit();
                    return true;
                }
                return false;
            });
        }

        if (!dataManager.hasBodyProfile()) {
            showBodyProfileDialog();
        }
    }

    public void openTab(int itemId) {
        if (bottomNav != null) {
            bottomNav.setSelectedItemId(itemId);
        }
    }

    private void showBodyProfileDialog() {
        LinearLayout form = new LinearLayout(this);
        form.setOrientation(LinearLayout.VERTICAL);
        int padding = (int) (20 * getResources().getDisplayMetrics().density);
        form.setPadding(padding, 8, padding, 0);

        EditText weight = new EditText(this);
        weight.setHint("Body weight in kg");
        weight.setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);
        form.addView(weight);

        EditText height = new EditText(this);
        height.setHint("Height in cm");
        height.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
        form.addView(height);

        Spinner goal = new Spinner(this);
        String[] goals = {"Lose weight - whole body", "Lose weight - specific areas", "Bulk", "Lean muscle", "Maintain fitness"};
        goal.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, goals));
        form.addView(goal);

        Spinner bodyType = new Spinner(this);
        String[] bodyTypes = {"Beginner", "Intermediate", "Advanced", "Returning after break"};
        bodyType.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, bodyTypes));
        form.addView(bodyType);

        new AlertDialog.Builder(this)
                .setTitle("Create your body profile")
                .setMessage("This helps optimize workout suggestions for your goal.")
                .setView(form)
                .setCancelable(false)
                .setPositiveButton("Save", (dialog, which) -> {
                    String profile = "Weight: " + weight.getText().toString().trim()
                            + " kg, Height: " + height.getText().toString().trim()
                            + " cm, Goal: " + goal.getSelectedItem()
                            + ", Level: " + bodyType.getSelectedItem();
                    dataManager.saveBodyProfile(profile);
                })
                .show();
    }
}
