package com.example.eggtracker.database;

import static java.lang.String.valueOf;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Database(entities = EggRecord.class, version = 1)
@TypeConverters({Converters.class})
public abstract class EggDatabase extends RoomDatabase {

    private static EggDatabase instance;

    public abstract EggRecordDao eggRecordDao();

    //synchronized means that only one thread at a time can access this method
    //this prevents from creating two databases
    public static synchronized EggDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    EggDatabase.class, "egg_database")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void>{
        private EggRecordDao eggRecordDao;

        private PopulateDbAsyncTask(EggDatabase db){
            eggRecordDao = db.eggRecordDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Log.d("TEST", "ROOM ONCREATE STARTED");
            LocalDate localDate = LocalDate.now();

            while(localDate.getYear() <= 2023){
                List<String> list = new ArrayList<>();

                for(int i = 0; i < YearMonth.from(localDate).lengthOfMonth() ; i++){
                    list.add("0");
                }
                EggRecord e = new EggRecord(valueOf(localDate.getYear()), localDate.getMonth().toString(), list);
                eggRecordDao.insert(e);

                localDate = localDate.plusMonths(1);
            }

            localDate = LocalDate.now().minusMonths(1);
            while(localDate.getYear() == 2022){
                List<String> list = new ArrayList<>();

                for(int i = 0; i < YearMonth.from(localDate).lengthOfMonth() ; i++){
                    list.add("0");
                }
                EggRecord e = new EggRecord(valueOf(localDate.getYear()), localDate.getMonth().toString(), list);
                eggRecordDao.insert(e);

                localDate = localDate.minusMonths(1);
            }

            Log.d("TEST", "ROOM ONCREATE FINISHED");
            return null;
        }
    }

}
