package com.example.eggtracker.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface EggRecordDao {

    @Insert
    void insert(EggRecord eggRecord);

    @Update
    void update(EggRecord eggRecord);

    @Delete
    void delete(EggRecord eggRecord);

    @Query("DELETE FROM eggs_table")
    void deleteAllEggRecords();

    @Query("SELECT * FROM eggs_table")
    LiveData<List<EggRecord>> getAllEggRecordsLiveData();

    @Query("SELECT * FROM eggs_table")
    List<EggRecord> getAllEggRecords();

    @Query("SELECT * from eggs_table WHERE year = :year AND month = :month")
    EggRecord getEggRecord(String year, String month);

}
