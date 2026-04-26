package com.example.workouttrackerr;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ViewHolder> {
    private final List<String[]> exercises;
    private final OnItemLongClickListener longClickListener;

    public interface OnItemLongClickListener {
        void onItemLongClick(String name, int position);
    }

    public ExerciseAdapter(List<String[]> exercises, OnItemLongClickListener longClickListener) {
        this.exercises = exercises;
        this.longClickListener = longClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exercise, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String[] exercise = exercises.get(position);
        holder.tvName.setText(exercise[0]);
        
        // Parse details: Muscle - Type - Equipment
        String details = exercise[1];
        String[] parts = details.split(" - ");
        if (parts.length >= 3) {
            holder.tvDetails.setText(parts[0] + " • " + parts[2]);
            holder.tvType.setText(parts[1]);
        } else {
            holder.tvDetails.setText(details);
            holder.tvType.setVisibility(View.GONE);
        }

        holder.itemView.setOnLongClickListener(v -> {
            longClickListener.onItemLongClick(exercise[0], position);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvDetails, tvType;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvExerciseName);
            tvDetails = itemView.findViewById(R.id.tvExerciseDetails);
            tvType = itemView.findViewById(R.id.tvExerciseType);
        }
    }
}
