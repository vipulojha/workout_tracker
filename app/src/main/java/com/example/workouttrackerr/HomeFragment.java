package com.example.workouttrackerr;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {
    private AppDataManager dataManager;
    private TextView workoutButtonText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        dataManager = new AppDataManager(requireContext());
        workoutButtonText = view.findViewById(R.id.tvStartWorkout);
        updateWorkoutButton();

        view.setAlpha(0f);
        view.setTranslationY(24f);
        view.animate().alpha(1f).translationY(0f).setDuration(420).start();

        view.findViewById(R.id.btnStartWorkout).setOnClickListener(v -> {
            v.animate().scaleX(0.96f).scaleY(0.96f).setDuration(80).withEndAction(() ->
                    v.animate().scaleX(1f).scaleY(1f).setDuration(120).start()).start();
            boolean active = dataManager.toggleWorkout();
            updateWorkoutButton();
            Toast.makeText(requireContext(), active ? "Workout started" : "Workout stopped", Toast.LENGTH_SHORT).show();
        });

        view.findViewById(R.id.btnViewPlans).setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).openTab(R.id.nav_plans);
            }
        });

        return view;
    }

    private void updateWorkoutButton() {
        boolean active = dataManager.isWorkoutActive();
        workoutButtonText.setText(active ? "Stop Workout" : "Start Workout");
        View container = (View) workoutButtonText.getParent();
        if (container instanceof CardView) {
            int colorRes = active ? R.color.danger : R.color.accent_primary;
            ((CardView) container).setCardBackgroundColor(getResources().getColor(colorRes, null));
        }
    }
}
