package com.example.workouttrackerr;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        view.setAlpha(0f);
        view.setTranslationY(24f);
        view.animate().alpha(1f).translationY(0f).setDuration(420).start();

        view.findViewById(R.id.btnStartWorkout).setOnClickListener(v -> {
            v.animate().scaleX(0.96f).scaleY(0.96f).setDuration(80).withEndAction(() ->
                    v.animate().scaleX(1f).scaleY(1f).setDuration(120).start()).start();
            Toast.makeText(requireContext(), "Workout started", Toast.LENGTH_SHORT).show();
        });

        view.findViewById(R.id.btnViewPlans).setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).openTab(R.id.nav_plans);
            }
        });

        return view;
    }
}
