package com.example.eggtracker.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

@Entity(tableName = "eggs_table")
public class EggRecord {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String year;
    private String month;
    private ArrayList<Integer> days;

    public EggRecord(String year, String month, ArrayList<Integer> days) {
        this.year = year;
        this.month = month;
        this.days = days;
    }

    public int getId() {
        return id;
    }

    public String getYear() {
        return year;
    }

    public String getMonth() {
        return month;
    }

    public ArrayList<Integer> getDays() {
        return days;
    }

    public void setId(int id) {
        this.id = id;
    }
}
