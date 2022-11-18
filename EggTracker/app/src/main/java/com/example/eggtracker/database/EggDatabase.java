package com.example.eggtracker.database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.ArrayList;

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
            String year = "2022";
            String month = "11";
            ArrayList<Integer> eggs_11 = new ArrayList<>();
            for(int i = 1; i <= 30; i++){
                eggs_11.add(1);
            }
            eggRecordDao.insert(new EggRecord(year, month, eggs_11));

            month = "12";
            ArrayList<Integer> eggs_12 = new ArrayList<>();
            for(int i = 1; i <= 31; i++){
                eggs_12.add(2);
            }
            eggRecordDao.insert(new EggRecord(year, month, eggs_12));

            return null;
        }
    }

}
