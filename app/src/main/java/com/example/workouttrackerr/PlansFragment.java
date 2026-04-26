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
        for (String[] plan : plans) {
            TextView item = createListItem(plan[0] + "\n" + plan[1]);
            plansContainer.addView(item);
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

    private void showCreatePlanDialog() {
        LinearLayout form = new LinearLayout(requireContext());
        form.setOrientation(LinearLayout.VERTICAL);
        form.setPadding(24, 8, 24, 0);

        EditText name = new EditText(requireContext());
        name.setHint("Plan name");
        form.addView(name);

        EditText details = new EditText(requireContext());
        details.setHint("Days per week and focus");
        form.addView(details);

        new AlertDialog.Builder(requireContext())
                .setTitle("Create workout plan")
                .setView(form)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Save", (dialog, which) -> {
                    String planName = name.getText().toString().trim();
                    String planDetails = details.getText().toString().trim();
                    if (!planName.isEmpty() && !planDetails.isEmpty()) {
                        dataManager.addPlan(planName, planDetails);
                        renderPlans();
                    }
                })
                .show();
    }
}
