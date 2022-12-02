package com.example.eggtracker;

import static java.lang.String.valueOf;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.eggtracker.database.EggRecord;
import com.example.eggtracker.repositories.EggRecordRepository;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

public class EggViewModel extends AndroidViewModel {

    private final EggRecordRepository repository;
    private LiveData<List<EggRecord>> allEggRecords;
    private EggRecord currentMonthRecord;
    private LocalDate selectedDate;

    public EggViewModel(@NonNull Application application) {
        super(application);
        repository = new EggRecordRepository(application);
        allEggRecords = repository.getAllEggRecordsLiveData();
    }

    //-DATABASE ACTIONS-
    public void insert(EggRecord eggRecord){
        repository.insert(eggRecord);
    }

    public void update(EggRecord eggRecord){
        repository.update(eggRecord);
    }

    public void delete(EggRecord eggRecord){
        repository.delete(eggRecord);
    }

    public void deleteAllEggRecords(){
        repository.deleteAllEggRecords();
    }

    public LiveData<List<EggRecord>> getAllEggRecordsLiveData(){
        return allEggRecords;
    }

    private List<EggRecord> getAllEggRecords(){
        return repository.getAllEggRecords();
    }

    private EggRecord getEggRecord(String year, String month){
        return repository.getEggRecord(year, month);
    }
    //-----------

    public void setSelectedDate(LocalDate selectedDate) {
        this.selectedDate = selectedDate;
        updateCurrentMonthRecord();
    }

    public LocalDate getSelectedDate() {
        return selectedDate;
    }

    public EggRecord getCurrentMonthRecord(){
        return currentMonthRecord;
    }

    private void updateCurrentMonthRecord(){
        EggRecord egg = getEggRecord(valueOf(selectedDate.getYear()), selectedDate.getMonth().toString());
        Log.d("TEST", "updateCurrentMonthRecord() -> getEggRecord: " + "year " + egg.getYear() + "; month " + egg.getMonth() + "; days " + egg.getDays().toString());
        currentMonthRecord = egg;
    }
}
