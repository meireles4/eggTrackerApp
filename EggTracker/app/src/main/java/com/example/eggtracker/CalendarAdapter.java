package com.example.eggtracker;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarViewHolder>
{

    public final ArrayList<String> eggsOfMonth;
    private final ArrayList<String> daysOfMonth;
    private final OnItemListener onItemListener;

    public CalendarAdapter(ArrayList<String> eggsOfMonth, ArrayList<String> daysOfMonth, OnItemListener onItemListener) {
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

        //I don't use this line because parent.getHeight() is not always the same for same screen
        //int viewHolderHeight = (int) (parent.getHeight() * 0.166666666);
        int viewHolderHeight = 300;

        layoutParams.height = viewHolderHeight;
        return new CalendarViewHolder(view, onItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {

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

    public void updateEggArray(int position, String value){
        eggsOfMonth.set(position, value);
    }

    public interface OnItemListener {
        void onItemClick(int position, String dayText, String eggCount);
    }
}
