package com.example.eggtracker.repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.eggtracker.database.EggDatabase;
import com.example.eggtracker.database.EggRecord;
import com.example.eggtracker.database.EggRecordDao;

import java.util.List;

public class EggRecordRepository {

    private EggRecordDao eggRecordDao;
    private LiveData<List<EggRecord>> allEggRocords;

    public EggRecordRepository(Application application){
        EggDatabase database = EggDatabase.getInstance(application);
        eggRecordDao = database.eggRecordDao();
        allEggRocords = eggRecordDao.getAllEggRecords();
    }

    public void insert(EggRecord eggRecord){
        new InsertEggRecordAsyncTask(eggRecordDao).execute(eggRecord);
    }

    public void update(EggRecord eggRecord){
        new UpdateEggRecordAsyncTask(eggRecordDao).execute(eggRecord);
    }

    public void delete(EggRecord eggRecord){
        new DeleteEggRecordAsyncTask(eggRecordDao).execute(eggRecord);
    }

    public void deleteAllEggRecords(){
        new DeleteAllEggRecordAsyncTask(eggRecordDao).execute();
    }

    public LiveData<List<EggRecord>> getAllEggRecords(){
        return allEggRocords;
    }

    private static class InsertEggRecordAsyncTask extends AsyncTask<EggRecord, Void, Void> {
        private EggRecordDao eggRecordDao;

        private InsertEggRecordAsyncTask(EggRecordDao eggRecordDao){
            this.eggRecordDao = eggRecordDao;
;        }

        @Override
        protected Void doInBackground(EggRecord... eggRecords) {
            eggRecordDao.insert(eggRecords[0]);
            return null;
        }
    }

    private static class UpdateEggRecordAsyncTask extends AsyncTask<EggRecord, Void, Void> {
        private EggRecordDao eggRecordDao;

        private UpdateEggRecordAsyncTask(EggRecordDao eggRecordDao){
            this.eggRecordDao = eggRecordDao;
        }

        @Override
        protected Void doInBackground(EggRecord... eggRecords) {
            eggRecordDao.update(eggRecords[0]);
            return null;
        }
    }

    private static class DeleteEggRecordAsyncTask extends AsyncTask<EggRecord, Void, Void> {
        private EggRecordDao eggRecordDao;

        private DeleteEggRecordAsyncTask(EggRecordDao eggRecordDao){
            this.eggRecordDao = eggRecordDao;
        }

        @Override
        protected Void doInBackground(EggRecord... eggRecords) {
            eggRecordDao.delete(eggRecords[0]);
            return null;
        }
    }

    private static class DeleteAllEggRecordAsyncTask extends AsyncTask<Void, Void, Void> {
        private EggRecordDao eggRecordDao;

        private DeleteAllEggRecordAsyncTask(EggRecordDao eggRecordDao){
            this.eggRecordDao = eggRecordDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            eggRecordDao.deleteAllEggRecords();
            return null;
        }
    }
}
