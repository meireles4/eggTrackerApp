package com.example.eggtracker;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarViewHolder>
{
    public List<String> eggsOfMonth;
    private final List<String> daysOfMonth;
    private final OnItemListener onItemListener;
    public int selectedPos = RecyclerView.NO_POSITION;

    public CalendarAdapter(List<String> eggsOfMonth, List<String> daysOfMonth, OnItemListener onItemListener) {
        this.eggsOfMonth = eggsOfMonth;
        this.daysOfMonth = daysOfMonth;
        this.onItemListener = onItemListener;
    }

    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.calendar_cell, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();

        int viewHolderHeight = parent.getHeight() / 6;

        layoutParams.height = viewHolderHeight;
        return new CalendarViewHolder(view, onItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {

        holder.itemView.setSelected(selectedPos == position);

        holder.dayOfMonth.setText(daysOfMonth.get(position));
        holder.eggCount.setText(eggsOfMonth.get(position));

        if(eggsOfMonth.get(position) == ""){
            holder.eggImage.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return daysOfMonth.size();
    }

    public interface OnItemListener {
        void onItemClick(int position, String dayText, String eggCount);
    }


}
