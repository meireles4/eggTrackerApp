package com.example.eggtracker.database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "eggs_table", primaryKeys = {"year","month"})
public class EggRecord {

    @NonNull
    private String year;
    @NonNull
    private String month;
    private List<String> days;

    public EggRecord( String year, String month, List<String> days) {
        this.year = year;
        this.month = month;
        this.days = days;
    }

    public String getYear() {
        return year;
    }

    public String getMonth() {
        return month;
    }

    public List<String> getDays() {
        return days;
    }

}
