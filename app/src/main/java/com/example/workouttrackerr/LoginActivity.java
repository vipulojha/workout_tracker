package com.example.workouttrackerr;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.OvershootInterpolator;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText etName, etEmail, etPassword, etConfirmPassword;
    private TextView tabLogin, tabSignup;
    private Button btnLogin;
    private boolean isSignupMode = false;
    private AuthManager authManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new AppSettings(this).applyTheme();
        setContentView(R.layout.activity_login);

        authManager = new AuthManager(this);
        if (authManager.isLoggedIn()) {
            openMainScreen();
            return;
        }

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        tabLogin = findViewById(R.id.tabLogin);
        tabSignup = findViewById(R.id.tabSignup);
        btnLogin = findViewById(R.id.btnLogin);

        View logo = findViewById(R.id.imgLogo);
        logo.setScaleX(0.82f);
        logo.setScaleY(0.82f);
        logo.animate()
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(650)
                .setInterpolator(new OvershootInterpolator())
                .start();

        tabLogin.setOnClickListener(v -> setSignupMode(false));
        tabSignup.setOnClickListener(v -> setSignupMode(true));

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString().trim();
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();
                String confirmPassword = etConfirmPassword.getText().toString();

                AuthManager.AuthResult result = isSignupMode
                        ? authManager.signup(name, email, password, confirmPassword)
                        : authManager.login(email, password);

                Toast.makeText(LoginActivity.this, result.message, Toast.LENGTH_SHORT).show();
                if (result.success) {
                    if (isSignupMode) {
                        setSignupMode(false);
                        etEmail.setText(email);
                        etPassword.setText("");
                        etConfirmPassword.setText("");
                    } else {
                        openMainScreen();
                    }
                }
            }
        });
    }

    private void setSignupMode(boolean signupMode) {
        isSignupMode = signupMode;
        etName.setVisibility(signupMode ? View.VISIBLE : View.GONE);
        etConfirmPassword.setVisibility(signupMode ? View.VISIBLE : View.GONE);
        btnLogin.setText(signupMode ? "Create Account" : "Continue");

        tabLogin.setBackgroundResource(signupMode ? R.drawable.bg_segment_unselected : R.drawable.bg_segment_selected);
        tabSignup.setBackgroundResource(signupMode ? R.drawable.bg_segment_selected : R.drawable.bg_segment_unselected);
        tabLogin.setTextColor(getColor(signupMode ? R.color.text_secondary : R.color.text_primary));
        tabSignup.setTextColor(getColor(signupMode ? R.color.text_primary : R.color.text_secondary));
    }

    private void openMainScreen() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
