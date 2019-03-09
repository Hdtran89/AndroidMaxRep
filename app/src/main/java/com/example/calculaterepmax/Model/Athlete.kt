package com.example.calculaterepmax.Model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "athletes_table")
data class Athlete(
    val dateOfWorkout: String,
    val exerciseName: String,
    val sets: String,
    val reps: String,
    val weight: String,
    val oneRepMax: Int
) : Serializable {
    @PrimaryKey(autoGenerate = true)
    var id:Int = 0
}