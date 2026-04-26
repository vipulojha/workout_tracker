package com.example.workouttrackerr;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class AwardAdapter extends RecyclerView.Adapter<AwardAdapter.ViewHolder> {
    private final List<Award> awards;
    private final OnAwardClickListener listener;

    public interface OnAwardClickListener {
        void onAwardClick(Award award);
    }

    public AwardAdapter(List<Award> awards, OnAwardClickListener listener) {
        this.awards = awards;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_award, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Award award = awards.get(position);
        holder.tvValue.setText(award.value);
        holder.tvTitle.setText(award.title);
        holder.tvYear.setText(award.year);

        // Apply custom gradient colors with null protection
        try {
            android.graphics.drawable.Drawable backgroundDrawable = holder.container.getBackground();
            if (backgroundDrawable instanceof android.graphics.drawable.InsetDrawable) {
                android.graphics.drawable.Drawable inner = ((android.graphics.drawable.InsetDrawable) backgroundDrawable).getDrawable();
                if (inner instanceof GradientDrawable) {
                    ((GradientDrawable) inner).setColors(new int[]{Color.parseColor(award.startColor), Color.parseColor(award.endColor)});
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.itemView.setOnClickListener(v -> listener.onAwardClick(award));
    }

    @Override
    public int getItemCount() {
        return awards.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvValue, tvTitle, tvYear;
        FrameLayout container;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvValue = itemView.findViewById(R.id.tvAwardValue);
            tvTitle = itemView.findViewById(R.id.tvAwardTitle);
            tvYear = itemView.findViewById(R.id.tvAwardYear);
            container = (FrameLayout) tvValue.getParent();
        }
    }

    public static class Award {
        public String title;
        public String year;
        public String value;
        public String startColor;
        public String endColor;
        public String description;

        public Award(String title, String year, String value, String startColor, String endColor, String description) {
            this.title = title;
            this.year = year;
            this.value = value;
            this.startColor = startColor;
            this.endColor = endColor;
            this.description = description;
        }
    }
}
