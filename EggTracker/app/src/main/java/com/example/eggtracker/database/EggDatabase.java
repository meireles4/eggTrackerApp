package com.example.eggtracker.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = EggRecord.class, version = 1)
@TypeConverters({Converters.class})
public abstract class EggDatabase extends RoomDatabase {

    private static EggDatabase instance;

    public abstract EggRecordDao eggRecordDao();

    //synchronized means that only one thread at a time can access this method
    //this prevents from creating two databases
    public static synchronized EggDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            EggDatabase.class, "egg_database")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }
}



//CODE USED TO INITIALIZE DB WHEN TESTING
/*
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

            return null;
        }
    }
*/

