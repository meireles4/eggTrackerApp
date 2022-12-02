package com.example.eggtracker;

import android.app.Dialog;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eggtracker.database.EggRecord;
import com.example.eggtracker.database.EggRecordDao;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements CalendarAdapter.OnItemListener {

    private EggViewModel eggViewModel;

    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calendarRecyclerView = findViewById(R.id.rv_calendar);
        monthYearText = findViewById(R.id.tv_monthYear);
        Button previousMonth = findViewById(R.id.bt_previousMonth);
        Button nextMonth = findViewById(R.id.bt_nextMonth);

        previousMonth.setOnClickListener(this::previousMonthAction);
        nextMonth.setOnClickListener(this::nextMonthAction);

        eggViewModel = new ViewModelProvider(this).get(EggViewModel.class);

        eggViewModel.getAllEggRecordsLiveData().observe(this, eggRecordList -> {
            Log.d("TEST", "ObserverLiveData: DataChanged");
        });

        //eggViewModel.deleteAllEggRecords();

        eggViewModel.setSelectedDate(LocalDate.now());
        setMonthView();
    }



    private void setMonthView() {

        //update year month text
        monthYearText.setText(monthYearFromDate(eggViewModel.getSelectedDate()));

        //build days array
        List<String> daysInMonth = daysInMonthArray(eggViewModel.getSelectedDate());

        //build eggs array
        List<String> eggsInMonth = eggsInMonthArray(daysInMonth);

        CalendarAdapter calendarAdapter = new CalendarAdapter(eggsInMonth, daysInMonth, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);

    }

    //Gets current month record from viewmodel and builds the array corresponding to the days
    private List<String> eggsInMonthArray(List<String> daysInMonth) {

        //get eggs list from DB
        List<String> list = eggViewModel.getCurrentMonthRecord().getDays();
        Log.d("TEST", "EggsInMonthArray() -> getCurrentMonth: " + list.toString());
        List<String> eggsInMonth = new ArrayList<>(list);

        //Fill with 0s before
        for(int i = 0; i < daysInMonth.size()-1; i++){
            if(daysInMonth.get(i) == ""){
                eggsInMonth.add(0, "");
            }
            else{
                break;
            }
        }

        //Fill with 0s after
        for(int i = daysInMonth.size()-1; i > -1; i--){
            if(daysInMonth.get(i) == ""){
                eggsInMonth.add("");
            }
            else{
                break;
            }
        }

        return eggsInMonth;
    }

    //Returns an array filled with empty spaces until day one is on the correct place.
    //Fills the remaining of the array with empty spaces.
    private List<String> daysInMonthArray(LocalDate date){

        List<String> daysInMonthArray = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(date);
        int daysInMonth = yearMonth.lengthOfMonth();

        //returns a copy of this LocalDate with the day-of-month altered
        LocalDate firstOfMonth = date.withDayOfMonth(1);
        int firstDayOfWeek = firstOfMonth.getDayOfWeek().getValue();

        for (int i = 1; i <= 38; i++){
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
        LocalDate newDate = eggViewModel.getSelectedDate().minusMonths(1);
        eggViewModel.setSelectedDate(newDate);
        setMonthView();
    }

    public void nextMonthAction(View view){
        LocalDate newDate = eggViewModel.getSelectedDate().plusMonths(1);
        eggViewModel.setSelectedDate(newDate);
        setMonthView();
    }


    @Override
    public void onItemClick(int position, String dayText, String eggCount) {

            String messageDay = dayText + " " + monthYearFromDate(eggViewModel.getSelectedDate());
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
                int dayChanged = Integer.parseInt(dayText.split(" ")[0]);

                EggRecord e = eggViewModel.getCurrentMonthRecord();
                List<String> l = e.getDays();
                l.set(dayChanged-1, updatedEggCount);
                EggRecord eNew = new EggRecord(e.getYear(), e.getMonth(), l);
                eggViewModel.update(eNew);

                //update calendar cell
                setMonthView();

                //dismiss dialog
                dialog.dismiss();
            }
        });

        dialog.show();
    }

}