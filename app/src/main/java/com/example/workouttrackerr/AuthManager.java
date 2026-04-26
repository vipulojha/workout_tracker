package com.example.workouttrackerr;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Patterns;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

public class AuthManager {
    private static final String PREFS_NAME = "workout_tracker_auth";
    private static final String KEY_NAME = "name";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PREVIOUS_EMAIL = "previous_email";
    private static final String KEY_PASSWORD_HASH = "password_hash";
    private static final String KEY_LOGGED_IN = "logged_in";
    private static final String KEY_REMEMBER_ME = "remember_me";

    private static final String DEMO_NAME = "Fitness Athlete";

    private final SharedPreferences prefs;

    public AuthManager(Context context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public AuthResult login(String inputIdentifier, String password, boolean rememberMe) {
        String identifier = inputIdentifier == null ? "" : inputIdentifier.trim();
        if (identifier.isEmpty()) {
            return AuthResult.error("Enter your email or username");
        }
        if (password == null || password.isEmpty()) {
            return AuthResult.error("Enter your password");
        }

        String savedEmail = getEmail();
        String savedName = getName();
        String previousEmail = prefs.getString(KEY_PREVIOUS_EMAIL, "");
        String savedPasswordHash = prefs.getString(KEY_PASSWORD_HASH, "");

        boolean identifierMatch = identifier.equalsIgnoreCase(savedEmail) || identifier.equalsIgnoreCase(savedName);
        
        // Use normalized email for hashing if it matches email, otherwise use the identifier
        String hashTarget = identifier.equalsIgnoreCase(savedEmail) ? normalizeEmail(identifier) : identifier;

        boolean currentLogin = Objects.equals(savedPasswordHash, hashPassword(hashTarget, password));
        boolean legacyLogin = Objects.equals(savedPasswordHash, hashLegacyPassword(hashTarget, password));
        boolean previousLegacyLogin = !previousEmail.isEmpty()
                && Objects.equals(savedPasswordHash, hashLegacyPassword(previousEmail, password));
        
        if (savedEmail.isEmpty()) {
            return AuthResult.error("No account found. Please sign up first");
        }
        if (!identifierMatch) {
            return AuthResult.error("No account found for this user");
        }
        if (!currentLogin && !legacyLogin && !previousLegacyLogin) {
            return AuthResult.error("Incorrect password");
        }

        SharedPreferences.Editor editor = prefs.edit()
                .putBoolean(KEY_LOGGED_IN, true)
                .putBoolean(KEY_REMEMBER_ME, rememberMe);
        if (legacyLogin || previousLegacyLogin) {
            editor.putString(KEY_PASSWORD_HASH, hashPassword(hashTarget, password));
            editor.remove(KEY_PREVIOUS_EMAIL);
        }
        editor.apply();
        return AuthResult.success("Login successful");
    }

    public AuthResult signup(String name, String email, String password, String confirmPassword) {
        name = name == null ? "" : name.trim();
        email = normalizeEmail(email);

        if (name.length() < 2) {
            return AuthResult.error("Enter your full name");
        }
        if (!isValidEmail(email)) {
            return AuthResult.error("Enter a valid email address");
        }
        if (password == null || password.length() < 6) {
            return AuthResult.error("Password must be at least 6 characters");
        }
        if (!Objects.equals(password, confirmPassword)) {
            return AuthResult.error("Passwords do not match");
        }
        if (Objects.equals(email, getEmail())) {
            return AuthResult.error("Account already exists. Please log in");
        }

        saveAccount(name, email, password, false);
        return AuthResult.success("Account created. Please log in");
    }

    public boolean isLoggedIn() {
        return prefs.getBoolean(KEY_LOGGED_IN, false);
    }

    public boolean isRemembered() {
        return prefs.getBoolean(KEY_REMEMBER_ME, false);
    }

    public String getName() {
        return prefs.getString(KEY_NAME, DEMO_NAME);
    }

    public String getUserId() {
        return prefs.getString(KEY_USER_ID, "FIT" + Math.abs(getName().hashCode() % 10000));
    }

    public String getEmail() {
        return prefs.getString(KEY_EMAIL, "");
    }

    public void logout() {
        prefs.edit().putBoolean(KEY_LOGGED_IN, false).apply();
    }

    public AuthResult updateProfile(String name, String userId, String email) {
        name = name == null ? "" : name.trim();
        userId = userId == null ? "" : userId.trim();
        email = normalizeEmail(email);
        if (name.length() < 2) {
            return AuthResult.error("Enter a valid name");
        }
        if (userId.isEmpty()) {
            return AuthResult.error("Enter a valid User ID");
        }
        if (!isValidEmail(email)) {
            return AuthResult.error("Enter a valid email address");
        }
        prefs.edit()
                .putString(KEY_NAME, name)
                .putString(KEY_USER_ID, userId)
                .putString(KEY_EMAIL, email)
                .putString(KEY_PREVIOUS_EMAIL, getEmail())
                .apply();
        return AuthResult.success("Profile updated");
    }

    public void deleteAccount() {
        prefs.edit().clear().apply();
    }

    private void saveAccount(String name, String email, String password, boolean loggedIn) {
        prefs.edit()
                .putString(KEY_NAME, name)
                .putString(KEY_EMAIL, email)
                .putString(KEY_PASSWORD_HASH, hashPassword(email, password))
                .putBoolean(KEY_LOGGED_IN, loggedIn)
                .apply();
    }

    private String normalizeEmail(String email) {
        return email == null ? "" : email.trim().toLowerCase();
    }

    private boolean isValidEmail(String email) {
        return !email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private String hashPassword(String email, String password) {
        return hashText(password);
    }

    private String hashLegacyPassword(String email, String password) {
        return hashText(email + ":" + password);
    }

    private String hashText(String value) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(value.getBytes(StandardCharsets.UTF_8));
            StringBuilder hex = new StringBuilder();
            for (byte b : hash) {
                hex.append(String.format("%02x", b));
            }
            return hex.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 is unavailable", e);
        }
    }

    public static class AuthResult {
        public final boolean success;
        public final String message;

        private AuthResult(boolean success, String message) {
            this.success = success;
            this.message = message;
        }

        static AuthResult success(String message) {
            return new AuthResult(true, message);
        }

        static AuthResult error(String message) {
            return new AuthResult(false, message);
        }
    }
}
