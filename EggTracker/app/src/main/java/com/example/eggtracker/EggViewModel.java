package com.example.eggtracker;

import static java.lang.String.valueOf;

import android.app.Application;

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
        EggRecord eggRecord = getEggRecord(valueOf(selectedDate.getYear()), selectedDate.getMonth().toString());

        if(eggRecord != null){
            currentMonthRecord = eggRecord;
        }
        else{
            eggRecord = newEmptyRecord();
            insert(eggRecord);
            currentMonthRecord = eggRecord;
        }
    }

    private EggRecord newEmptyRecord(){
        List<String> l = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(selectedDate);
        int daysInMonth = yearMonth.lengthOfMonth();

        for( int i = 0; i < daysInMonth; i++){
            l.add("0");
        }

        EggRecord e = new EggRecord(valueOf(selectedDate.getYear()), selectedDate.getMonth().toString(), l);
        return e;
    }
}
