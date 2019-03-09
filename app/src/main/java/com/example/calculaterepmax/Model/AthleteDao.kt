package com.example.calculaterepmax.Model
import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query

@Dao
interface AthleteDao {
    @Insert
    fun insert(athlete:Athlete)

    @Query("DELETE FROM athletes_table")
    fun deleteAllAthletes()

    @Query("SELECT * FROM athletes_table")
    fun getAllAthletes(): LiveData<List<Athlete>>

}