package com.example.eggtracker;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements CalendarAdapter.OnItemListener {

    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private LocalDate selectedDate;
    private ArrayList<String> eggsInMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initWidgets();
        selectedDate = LocalDate.now();
        setMonthView(null);

        Button previousMonth = findViewById(R.id.bt_previousMonth);
        Button nextMonth = findViewById(R.id.bt_nextMonth);

        previousMonth.setOnClickListener(this::previousMonthAction);
        nextMonth.setOnClickListener(this::nextMonthAction);

    }

    private void initWidgets() {
        calendarRecyclerView = findViewById(R.id.rv_calendar);
        monthYearText = findViewById(R.id.tv_monthYear);
    }

    private void setMonthView(ArrayList<String> eggsArray) {
        monthYearText.setText(monthYearFromDate(selectedDate));
        ArrayList<String> daysInMonth = daysInMonthArray(selectedDate);

        if(eggsArray == null){
            eggsInMonth = eggsInMonthArray(daysInMonth);
        }
        else{
            eggsInMonth = eggsArray;
        }

        CalendarAdapter calendarAdapter = new CalendarAdapter(eggsInMonth, daysInMonth, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }

    private ArrayList<String> eggsInMonthArray(ArrayList<String> daysInMonth) {
        ArrayList<String> eggsInMonth = new ArrayList<>();

        for(int i = 0; i < daysInMonth.size(); i++){
            if(daysInMonth.get(i) == ""){
                eggsInMonth.add("");
            }
            else{
                eggsInMonth.add("0");
            }
        }

        return eggsInMonth;
    }

    private ArrayList<String> daysInMonthArray(LocalDate date){

        ArrayList<String> daysInMonthArray = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(date);

        int daysInMonth = yearMonth.lengthOfMonth();

        //returns a copy of this LocalDate with the day-of-month altered
        LocalDate firstOfMonth = selectedDate.withDayOfMonth(1);
        int firstDayOfWeek = firstOfMonth.getDayOfWeek().getValue();


        for (int i = 1; i <= 38; i++){
            //----------
            //This if statement fixes an edge case causing 1st day of month appear in second row
            //leaving first row empty
            if(firstDayOfWeek == 7){
                if(i > daysInMonth){
                    daysInMonthArray.add("");
                }
                else{
                    daysInMonthArray.add(String.valueOf(i));
                }
            }
            //----------
            else{
                if(i <= firstDayOfWeek || i > daysInMonth + firstDayOfWeek){
                    daysInMonthArray.add("");
                }
                else{
                    daysInMonthArray.add(String.valueOf(i - firstDayOfWeek));
                }
            }


        }

        return daysInMonthArray;
    }

    private String monthYearFromDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        return date.format(formatter);
    }

    public void previousMonthAction(View view){
        if(selectedDate.minusMonths(1).getYear() < 2022){
            return;
        }
        selectedDate = selectedDate.minusMonths(1);
        setMonthView(null);
    }

    public void nextMonthAction(View view){
        if(selectedDate.plusMonths(1).getYear() > 2023){
            return;
        }
        selectedDate = selectedDate.plusMonths(1);
        setMonthView(null);
    }


    @Override
    public void onItemClick(int position, String dayText, String eggCount) {

            String messageDay = dayText + " " + monthYearFromDate(selectedDate);
            if(dayText != ""){
                showUpdateDialog(position ,messageDay, eggCount);
            }
    }

    private void showUpdateDialog(int position, String dayText, String eggCount) {

        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.activity_update_count);

        TextView dateTextView = dialog.findViewById(R.id.tv_date);
        dateTextView.setText(dayText);

        EditText eggEditText = dialog.findViewById(R.id.et_eggcount);
        eggEditText.setText(eggCount);

        Button updateButton = dialog.findViewById(R.id.bt_update);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get new number
                String updatedEggCount = eggEditText.getText().toString();

                //Update master array
                eggsInMonth.set(position, updatedEggCount);

                //update calendar cell
                setMonthView(eggsInMonth);

                //dismiss dialog
                dialog.dismiss();
            }
        });

        dialog.show();
    }

}