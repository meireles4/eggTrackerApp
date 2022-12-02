package com.example.eggtracker.database;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.room.TypeConverter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Converters {

    @TypeConverter
    public static List<String> fromString (String value){

        //Log.d("TEST", "CONVERTER fromString STRING FROM DB: " + value);
        String[] parsed = value.split(",");
        //Log.d("TEST", "CONVERTER fromString RETURNED STRING: " + Arrays.asList(parsed).toString());
        return Arrays.asList(parsed);
    }

    @TypeConverter
    public static String toString(List<String> list) {

        StringBuilder s = new StringBuilder();

        s.append(list.get(0));
        for(int i = 1; i < list.size(); i++){
            s.append(",").append(list.get(i));
        }

        //Log.d("TEST", "CONVERTER toString: " + s.toString());
        return s.toString();
    }

}
