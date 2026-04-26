package com.example.workouttrackerr;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class PerformanceFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_performance, container, false);

        RecyclerView rvAwards = view.findViewById(R.id.rvAwards);
        rvAwards.setLayoutManager(new GridLayoutManager(requireContext(), 3));
        
        List<AwardAdapter.Award> awards = new ArrayList<>();
        awards.add(new AwardAdapter.Award("April", "2026", "4", "#18C29C", "#5B42F3", "Completed all challenges set for April 2026."));
        awards.add(new AwardAdapter.Award("February", "2026", "2", "#3498DB", "#2980B9", "Achievement for consistency in February."));
        awards.add(new AwardAdapter.Award("January", "2026", "1", "#E67E22", "#D35400", "Start of the year fitness burst."));
        awards.add(new AwardAdapter.Award("October", "2025", "10", "#F1C40F", "#F39C12", "Autumn strength challenge completed."));
        awards.add(new AwardAdapter.Award("September", "2025", "9", "#E74C3C", "#C0392B", "Max volume month reached."));

        AwardAdapter adapter = new AwardAdapter(awards, this::showAwardDetail);
        rvAwards.setAdapter(adapter);

        return view;
    }

    private void showAwardDetail(AwardAdapter.Award award) {
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.item_award, null);
        ((TextView) dialogView.findViewById(R.id.tvAwardValue)).setText(award.value);
        ((TextView) dialogView.findViewById(R.id.tvAwardTitle)).setText(award.title + " Challenge");
        ((TextView) dialogView.findViewById(R.id.tvAwardYear)).setText("Earned in " + award.title + " " + award.year);
        
        // Update background of the badge in dialog
        View container = (View) dialogView.findViewById(R.id.tvAwardValue).getParent();
        android.graphics.drawable.GradientDrawable background = (android.graphics.drawable.GradientDrawable) 
                ((android.graphics.drawable.InsetDrawable) container.getBackground()).getDrawable();
        background.setColors(new int[]{android.graphics.Color.parseColor(award.startColor), android.graphics.Color.parseColor(award.endColor)});

        new AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .setTitle("Achievement Unlocked!")
                .setMessage(award.description + "\n\nThis award recognizes your dedication to your fitness journey.")
                .setPositiveButton("Close", null)
                .show();
    }
}
