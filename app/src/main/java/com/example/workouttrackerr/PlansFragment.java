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
import java.util.List;

public class PlansFragment extends Fragment {
    private AppDataManager dataManager;
    private LinearLayout plansContainer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_plans, container, false);
        dataManager = new AppDataManager(requireContext());
        plansContainer = view.findViewById(R.id.plansContainer);
        view.findViewById(R.id.btnCreatePlan).setOnClickListener(v -> showCreatePlanDialog());
        renderPlans();
        return view;
    }

    private void renderPlans() {
        plansContainer.removeAllViews();
        List<String[]> plans = dataManager.getPlans();
        for (int i = 0; i < plans.size(); i++) {
            final int index = i;
            String[] plan = plans.get(i);
            TextView item = createListItem(plan[0] + "\n" + plan[1]);
            item.setOnLongClickListener(v -> {
                showDeleteDialog(plan[0], index);
                return true;
            });
            plansContainer.addView(item);
        }
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
