package com.example.eggtracker.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

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

    @Query("SELECT * FROM eggs_table ORDER BY year, month DESC")
    LiveData<List<EggRecord>> getAllEggRecords();



}
