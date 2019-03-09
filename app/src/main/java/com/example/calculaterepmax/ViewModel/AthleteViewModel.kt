package com.example.calculaterepmax.ViewModel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import com.example.calculaterepmax.Model.Athlete
import com.example.calculaterepmax.Model.AthleteRepository

class AthleteViewModel(application: Application): AndroidViewModel(application) {
    private var repository: AthleteRepository = AthleteRepository(application)
    private var allAthletes: LiveData<List<Athlete>> = repository.getAllAthletes()

    fun insert(athlete: Athlete){
        repository.insert(athlete)
    }

    fun deleteAllAthletes(){
        repository.deleteAllAthlete()
    }

    fun getAllAthletes(): LiveData<List<Athlete>>{
        return allAthletes
    }
}