package com.example.eggtracker;

import static java.lang.String.valueOf;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.eggtracker.database.EggRecord;
import com.google.android.material.appbar.AppBarLayout;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CalendarAdapter.OnItemListener {

    private EggViewModel eggViewModel;

    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    CalendarAdapter calendarAdapter;
    private boolean firstTimeOpeningApp = true;

    DatePickerDialog picker;

    LottieAnimationView leftArrowAnim, rightArrowAnim;

    List<String> daysInMonth, eggsInMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calendarRecyclerView = findViewById(R.id.rv_calendar);
        layoutManager = new GridLayoutManager(getApplicationContext(), 7);

        monthYearText = findViewById(R.id.tv_monthYear);

        leftArrowAnim = findViewById(R.id.animationViewPreviousMonth);
        leftArrowAnim.setOnClickListener(this::previousMonthAction);
        rightArrowAnim = findViewById(R.id.animationViewNextMonth);
        rightArrowAnim.setOnClickListener(this::nextMonthAction);

        eggViewModel = new ViewModelProvider(this).get(EggViewModel.class);

        initializeMenuItems();

        eggViewModel.setSelectedDate(LocalDate.now());
        setMonthView();
    }

    private void initializeMenuItems() {

        //Item for searching dates
        ActionMenuItemView topMenuCalendar = findViewById(R.id.menu_calendar);
        topMenuCalendar.setOnClickListener(view -> {

            int day = eggViewModel.getSelectedDate().getDayOfMonth();
            int month = eggViewModel.getSelectedDate().getMonth().getValue()-1;
            int year = eggViewModel.getSelectedDate().getYear();

            picker = new DatePickerDialog(MainActivity.this,
                    (view1, year1, monthOfYear, dayOfMonth) -> {
                        //monthOfYear = [0-11]. That's why we add +1
                        LocalDate localDate = LocalDate.of(year1, monthOfYear+1, dayOfMonth);
                        eggViewModel.setSelectedDate(localDate);
                        firstTimeOpeningApp = true;
                        setMonthView();
                    }, year, month, day);

            picker.show();
        });

        //Item for returning to today
        ActionMenuItemView topMenuReturnToToday = findViewById(R.id.menu_today);
        topMenuReturnToToday.setOnClickListener( view -> {

            eggViewModel.setSelectedDate(LocalDate.now());
            firstTimeOpeningApp = true;
            setMonthView();

        });
    }

    private void setMonthView() {

        //update year month text
        monthYearText.setText(monthYearFromDate(eggViewModel.getSelectedDate()));

        //build days array
        daysInMonth = daysInMonthArray(eggViewModel.getSelectedDate());

        //build eggs array
        eggsInMonth = eggsInMonthArray();

        calendarAdapter = new CalendarAdapter(eggsInMonth, daysInMonth, this);
        if(firstTimeOpeningApp){
            int day = daysInMonth.indexOf(valueOf(eggViewModel.getSelectedDate().getDayOfMonth()));
            calendarAdapter.selectedPos = day ;
            firstTimeOpeningApp = false;
        }

        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }

    //Gets current month record from viewmodel and builds the array corresponding to the days
    private List<String> eggsInMonthArray() {

        //get eggs list from DB
        List<String> list = eggViewModel.getCurrentMonthRecord().getDays();
        List<String> eggsList = new ArrayList<>(list);

        //Fill with 0s before
        for(int i = 0; i < daysInMonth.size()-1; i++){
            if(daysInMonth.get(i) == ""){
                eggsList.add(0, "");
            }
            else{
                break;
            }
        }

        //Fill with 0s after
        for(int i = daysInMonth.size()-1; i > -1; i--){
            if(daysInMonth.get(i) == ""){
                eggsList.add("");
            }
            else{
                break;
            }
        }

        return eggsList;
    }

    //Returns an array filled with empty spaces until day one is on the correct place.
    //Fills the remaining of the array with empty spaces.
    private List<String> daysInMonthArray(LocalDate date){

        List<String> daysInMonthArray = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(date);
        int daysInMonthLength = yearMonth.lengthOfMonth();

        //returns a copy of this LocalDate with the day-of-month altered
        LocalDate firstOfMonth = date.withDayOfMonth(1);
        int firstDayOfWeek = firstOfMonth.getDayOfWeek().getValue();

        for (int i = 1; i <= 42; i++){
            //This if statement fixes an edge case causing 1st day of month appear in second row
            //leaving first row empty
            if(firstDayOfWeek == 7){
                if(i > daysInMonthLength){
                    daysInMonthArray.add("");
                }
                else{
                    daysInMonthArray.add(valueOf(i));
                }
            }
            //----------
            else{
                if(i <= firstDayOfWeek || i > daysInMonthLength + firstDayOfWeek){
                    daysInMonthArray.add("");
                }
                else{
                    daysInMonthArray.add(valueOf(i - firstDayOfWeek));
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
        leftArrowAnim.playAnimation();
        LocalDate newDate = eggViewModel.getSelectedDate().minusMonths(1);
        eggViewModel.setSelectedDate(newDate);
        setMonthView();
    }

    public void nextMonthAction(View view){
        rightArrowAnim.playAnimation();
        LocalDate newDate = eggViewModel.getSelectedDate().plusMonths(1);
        eggViewModel.setSelectedDate(newDate);
        setMonthView();
    }


    @Override
    public void onItemClick(int position, String dayText, String eggCount) {

        calendarAdapter.notifyItemChanged(calendarAdapter.selectedPos);
        calendarAdapter.selectedPos = position;
        calendarAdapter.notifyItemChanged(position);

        LocalDate selectedDate = LocalDate.of(eggViewModel.getSelectedDate().getYear(),
                eggViewModel.getSelectedDate().getMonth().getValue(),
                Integer.parseInt(dayText));
        eggViewModel.setSelectedDate(selectedDate);

        String messageDay = dayText + " " + monthYearFromDate(eggViewModel.getSelectedDate());
        if(dayText != ""){
            showUpdateDialog(position ,messageDay, eggCount);
        }
    }

    private void showUpdateDialog(int position, String dayText, String eggCount) {

        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.activity_update_count);
        dialog.getWindow().getAttributes().windowAnimations = R.style.animation;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenHeight = displayMetrics.heightPixels;
        int screenWidth = displayMetrics.widthPixels;
        dialog.getWindow().getAttributes().width = screenWidth / 2;
        dialog.getWindow().getAttributes().height = screenHeight / 2;

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

                //Get direct reference of viewModel property. Change it directly.
                //No need to set the value with a setter function or use setSelectedDate()
                List<String> l = e.getDays();

                l.set(dayChanged-1, updatedEggCount);
                EggRecord eNew = new EggRecord(e.getYear(), e.getMonth(), l);
                eggViewModel.update(eNew);

                eggsInMonth.set(position, updatedEggCount);
                calendarAdapter.notifyItemChanged(position);

                //dismiss dialog
                dialog.dismiss();
            }
        });

        dialog.show();
    }

}