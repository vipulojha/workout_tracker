package com.example.workouttrackerr;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment {
    private AuthManager authManager;
    private TextView name;
    private TextView email;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        authManager = new AuthManager(requireContext());

        name = view.findViewById(R.id.tvProfileName);
        email = view.findViewById(R.id.tvProfileEmail);
        refreshProfile();

        view.findViewById(R.id.btnEditProfile).setOnClickListener(v -> showEditProfileDialog());

        view.findViewById(R.id.btnBodyProfile).setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), OnboardingActivity.class));
        });

        view.findViewById(R.id.btnSettings).setOnClickListener(v -> showSettingsDialog());

        view.findViewById(R.id.btnLogout).setOnClickListener(v -> {
            authManager.logout();
            Intent intent = new Intent(requireContext(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            requireActivity().finish();
        });

        return view;
    }

    private void refreshProfile() {
        name.setText(authManager.getName());
        email.setText(authManager.getEmail().isEmpty() ? "No email saved" : authManager.getEmail());
    }

    private void showEditProfileDialog() {
        LinearLayout form = new LinearLayout(requireContext());
        form.setOrientation(LinearLayout.VERTICAL);
        form.setPadding(24, 8, 24, 0);

        EditText editName = new EditText(requireContext());
        editName.setHint("Display name");
        editName.setText(authManager.getName());
        form.addView(editName);

        EditText editUserId = new EditText(requireContext());
        editUserId.setHint("User ID");
        editUserId.setText(authManager.getUserId());
        form.addView(editUserId);

        EditText editEmail = new EditText(requireContext());
        editEmail.setHint("Email id");
        editEmail.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        editEmail.setText(authManager.getEmail());
        form.addView(editEmail);

        new AlertDialog.Builder(requireContext())
                .setTitle("Edit profile")
                .setView(form)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Save", (dialog, which) -> {
                    AuthManager.AuthResult result = authManager.updateProfile(
                            editName.getText().toString(),
                            editUserId.getText().toString(),
                            editEmail.getText().toString()
                    );
                    Toast.makeText(requireContext(), result.message, Toast.LENGTH_SHORT).show();
                    if (result.success) {
                        refreshProfile();
                    }
                })
                .show();
    }

    private void showSettingsDialog() {
        String[] options = {"Light theme", "Dark theme", "Delete account"};
        new AlertDialog.Builder(requireContext())
                .setTitle("Settings")
                .setItems(options, (dialog, which) -> {
                    if (which == 0) {
                        new AppSettings(requireContext()).setTheme(AppSettings.THEME_LIGHT);
                        requireActivity().recreate();
                    } else if (which == 1) {
                        new AppSettings(requireContext()).setTheme(AppSettings.THEME_DARK);
                        requireActivity().recreate();
                    } else {
                        confirmDeleteAccount();
                    }
                })
                .show();
    }

    private void confirmDeleteAccount() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Delete account?")
                .setMessage("This removes your saved login, body profile, custom plans, and custom exercises from this device.")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Delete", (dialog, which) -> {
                    authManager.deleteAccount();
                    new AppDataManager(requireContext()).clear();
                    new AppSettings(requireContext()).clear();
                    Intent intent = new Intent(requireContext(), LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    requireActivity().finish();
                })
                .show();
    }
}
