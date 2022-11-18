package com.example.eggtracker.database;

import androidx.room.TypeConverter;

import java.util.ArrayList;

public class Converters {

    @TypeConverter
    public static ArrayList<Integer> fromString (String value){
        String[] parsed = value.split(",");
        ArrayList<Integer> arrayList = new ArrayList<>();

        for(int i = 0; i < parsed.length; i++){
            arrayList.add(Integer.parseInt(parsed[i]));
        }

        return arrayList;
    }

    @TypeConverter
    public static String toString(ArrayList<Integer> list) {
        StringBuilder s = new StringBuilder(new String());

        //first position will be with a ",". PROBLEM?? Check in the future
        for(int i = 0; i < list.size(); i++){
            s.append(",").append(list.get(i).toString());
        }

        return s.toString();
    }

}
