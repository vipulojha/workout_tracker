package com.example.workouttrackerr;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        AuthManager authManager = new AuthManager(requireContext());

        TextView name = view.findViewById(R.id.tvProfileName);
        TextView email = view.findViewById(R.id.tvProfileEmail);
        name.setText(authManager.getName());
        email.setText(authManager.getEmail().isEmpty() ? "No email saved" : authManager.getEmail());

        view.findViewById(R.id.btnEditProfile).setOnClickListener(v ->
                Toast.makeText(requireContext(), "Profile editor ready for the next version", Toast.LENGTH_SHORT).show());

        view.findViewById(R.id.btnSettings).setOnClickListener(v ->
                Toast.makeText(requireContext(), "Settings are up to date", Toast.LENGTH_SHORT).show());

        view.findViewById(R.id.btnLogout).setOnClickListener(v -> {
            authManager.logout();
            Intent intent = new Intent(requireContext(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            requireActivity().finish();
        });

        return view;
    }
}
