package com.example.calculaterepmax.Model

import android.app.Application
import android.arch.lifecycle.LiveData
import android.os.AsyncTask

class AthleteRepository(application: Application) {
    private var athleteDao : AthleteDao
    private var allAthletes: LiveData<List<Athlete>>

    init {
        val database : AthleteDatabase = AthleteDatabase.getInstance(
            application.applicationContext
        )!!
        athleteDao = database.athleteDao()
        allAthletes = athleteDao.getAllAthletes()
    }

    fun insert(athlete: Athlete){
        InsertAthleteAsyncTask(
            athleteDao
        ).execute(athlete)
    }

    fun deleteAllAthlete(){
        DeleteAthleteAsyncTask(
            athleteDao
        ).execute()
    }

    fun getAllAthletes(): LiveData<List<Athlete>>{
        return allAthletes
    }

    private class InsertAthleteAsyncTask(athleteDao: AthleteDao) : AsyncTask<Athlete,Unit,Unit>() {
        val athleteDao = athleteDao
        override fun doInBackground(vararg athlete: Athlete?) {
            athleteDao.insert(athlete[0]!!)
        }
    }

    private class DeleteAthleteAsyncTask(val athleteDao: AthleteDao) : AsyncTask<Unit,Unit,Unit>() {
        override fun doInBackground(vararg params: Unit?) {
            athleteDao.deleteAllAthletes()
        }
    }
}