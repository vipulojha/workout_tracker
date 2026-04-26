package com.example.workouttrackerr;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import java.util.List;

public class ExercisesFragment extends Fragment {
    private AppDataManager dataManager;
    private LinearLayout exercisesContainer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exercises, container, false);
        dataManager = new AppDataManager(requireContext());
        exercisesContainer = view.findViewById(R.id.exercisesContainer);
        view.findViewById(R.id.btnCreateExercise).setOnClickListener(v -> showCreateExerciseDialog());
        renderExercises();
        return view;
    }

    private void renderExercises() {
        exercisesContainer.removeAllViews();
        List<String[]> exercises = dataManager.getExercises();
        for (String[] exercise : exercises) {
            TextView item = createListItem(exercise[0] + "\n" + exercise[1]);
            exercisesContainer.addView(item);
        }
    }

    private TextView createListItem(String text) {
        TextView item = new TextView(requireContext());
        item.setText(text);
        item.setTextColor(getResources().getColor(R.color.text_primary, null));
        item.setTextSize(16);
        item.setBackgroundResource(R.drawable.bg_auth_field);
        item.setPadding(18, 18, 18, 18);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 0, 0, 14);
        item.setLayoutParams(params);
        return item;
    }

    private void showCreateExerciseDialog() {
        LinearLayout form = new LinearLayout(requireContext());
        form.setOrientation(LinearLayout.VERTICAL);
        form.setPadding(24, 8, 24, 0);

        EditText name = new EditText(requireContext());
        name.setHint("Exercise name");
        form.addView(name);

        EditText details = new EditText(requireContext());
        details.setHint("Muscle group - type - equipment");
        form.addView(details);

        new AlertDialog.Builder(requireContext())
                .setTitle("Add exercise")
                .setView(form)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Save", (dialog, which) -> {
                    String exerciseName = name.getText().toString().trim();
                    String exerciseDetails = details.getText().toString().trim();
                    if (!exerciseName.isEmpty() && !exerciseDetails.isEmpty()) {
                        dataManager.addExercise(exerciseName, exerciseDetails);
                        renderExercises();
                    }
                })
                .show();
    }
}
