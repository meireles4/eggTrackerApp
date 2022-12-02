package com.example.eggtracker.repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.eggtracker.database.EggDatabase;
import com.example.eggtracker.database.EggRecord;
import com.example.eggtracker.database.EggRecordDao;

import java.util.ArrayList;
import java.util.List;

public class EggRecordRepository {

    private EggRecordDao eggRecordDao;
    private LiveData<List<EggRecord>> allEggRocordsLiveData;

    public EggRecordRepository(Application application){
        EggDatabase database = EggDatabase.getInstance(application);
        eggRecordDao = database.eggRecordDao();
        allEggRocordsLiveData = eggRecordDao.getAllEggRecordsLiveData();
    }

    public void insert(@NonNull EggRecord eggRecord){
        new InsertEggRecordAsyncTask(eggRecordDao).execute(eggRecord);
    }

    public void update(@NonNull EggRecord eggRecord){
        new UpdateEggRecordAsyncTask(eggRecordDao).execute(eggRecord);
    }

    public void delete(@NonNull EggRecord eggRecord){
        new DeleteEggRecordAsyncTask(eggRecordDao).execute(eggRecord);
    }

    public void deleteAllEggRecords(){
        new DeleteAllEggRecordAsyncTask(eggRecordDao).execute();
    }

    public LiveData<List<EggRecord>> getAllEggRecordsLiveData(){
        return allEggRocordsLiveData;
    }

    //TODO: Maybe create an async task
    public List<EggRecord> getAllEggRecords(){
        return eggRecordDao.getAllEggRecords();
    }

    //TODO: Maybe create an async task
    public EggRecord getEggRecord(String year, String month) {
        return eggRecordDao.getEggRecord(year, month);
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
