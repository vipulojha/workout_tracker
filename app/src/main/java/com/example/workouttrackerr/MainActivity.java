package com.example.workouttrackerr;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!new AuthManager(this).isLoggedIn()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }
        setContentView(R.layout.activity_main);

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
    }

    public void openTab(int itemId) {
        if (bottomNav != null) {
            bottomNav.setSelectedItemId(itemId);
        }
    }
}
