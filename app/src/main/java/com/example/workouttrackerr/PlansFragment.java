package com.example.workouttrackerr;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class PlansFragment extends Fragment {
    private AppDataManager dataManager;
    private RecyclerView rvPlans;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_plans, container, false);
        dataManager = new AppDataManager(requireContext());
        rvPlans = view.findViewById(R.id.rvPlans);
        rvPlans.setLayoutManager(new LinearLayoutManager(requireContext()));
        
        view.findViewById(R.id.btnCreatePlan).setOnClickListener(v -> showCreatePlanDialog());
        renderPlans();
        return view;
    }

    private void renderPlans() {
        List<String[]> plans = dataManager.getPlans();
        WorkoutAdapter adapter = new WorkoutAdapter(plans, this::showDeleteDialog);
        rvPlans.setAdapter(adapter);
    }

    private void showDeleteDialog(String name, int index) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Delete plan")
                .setMessage("Are you sure you want to delete \"" + name + "\"?")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Delete", (dialog, which) -> {
                    dataManager.deletePlan(index);
                    renderPlans();
                })
                .show();
    }

    private void showCreatePlanDialog() {
        LinearLayout form = new LinearLayout(requireContext());
        form.setOrientation(LinearLayout.VERTICAL);
        form.setPadding(24, 8, 24, 0);

        EditText name = new EditText(requireContext());
        name.setHint("Plan name (e.g. Upper Body)");
        form.addView(name);

        Spinner days = new Spinner(requireContext());
        String[] dayOptions = {"1 day/week", "2 days/week", "3 days/week", "4 days/week", "5 days/week", "6 days/week", "7 days/week"};
        days.setAdapter(new android.widget.ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, dayOptions));
        form.addView(days);

        EditText focus = new EditText(requireContext());
        focus.setHint("Focus (e.g. Hypertrophy)");
        form.addView(focus);

        new AlertDialog.Builder(requireContext())
                .setTitle("Create workout plan")
                .setView(form)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Save", (dialog, which) -> {
                    String planName = name.getText().toString().trim();
                    String planDetails = days.getSelectedItem() + " - " + focus.getText().toString().trim();
                    if (!planName.isEmpty()) {
                        dataManager.addPlan(planName, planDetails);
                        renderPlans();
                    }
                })
                .show();
    }
}
