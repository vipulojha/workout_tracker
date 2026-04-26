package com.example.workouttrackerr;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ExercisesFragment extends Fragment {
    private AppDataManager dataManager;
    private RecyclerView rvExercises;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exercises, container, false);
        dataManager = new AppDataManager(requireContext());
        rvExercises = view.findViewById(R.id.rvExercises);
        rvExercises.setLayoutManager(new LinearLayoutManager(requireContext()));
        
        view.findViewById(R.id.btnCreateExercise).setOnClickListener(v -> showCreateExerciseDialog());
        renderExercises();
        return view;
    }

    private void renderExercises() {
        List<String[]> exercises = dataManager.getExercises();
        ExerciseAdapter adapter = new ExerciseAdapter(exercises, this::showDeleteDialog);
        rvExercises.setAdapter(adapter);
    }

    private void showDeleteDialog(String name, int index) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Delete exercise")
                .setMessage("Are you sure you want to delete \"" + name + "\"?")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Delete", (dialog, which) -> {
                    dataManager.deleteExercise(index);
                    renderExercises();
                })
                .show();
    }

    private void showCreateExerciseDialog() {
        LinearLayout form = new LinearLayout(requireContext());
        form.setOrientation(LinearLayout.VERTICAL);
        form.setPadding(24, 8, 24, 0);

        EditText name = new EditText(requireContext());
        name.setHint("Exercise name");
        form.addView(name);

        EditText muscle = new EditText(requireContext());
        muscle.setHint("Muscle group (e.g. Chest)");
        form.addView(muscle);

        Spinner type = new Spinner(requireContext());
        String[] typeOptions = {"Strength", "Hypertrophy", "Endurance", "Flexibility"};
        type.setAdapter(new android.widget.ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, typeOptions));
        form.addView(type);

        EditText equipment = new EditText(requireContext());
        equipment.setHint("Equipment (e.g. Barbell)");
        form.addView(equipment);

        new AlertDialog.Builder(requireContext())
                .setTitle("Add exercise")
                .setView(form)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Save", (dialog, which) -> {
                    String exerciseName = name.getText().toString().trim();
                    String exerciseDetails = muscle.getText().toString().trim() + " - " 
                            + type.getSelectedItem() + " - " 
                            + equipment.getText().toString().trim();
                    if (!exerciseName.isEmpty()) {
                        dataManager.addExercise(exerciseName, exerciseDetails);
                        renderExercises();
                    }
                })
                .show();
    }
}
