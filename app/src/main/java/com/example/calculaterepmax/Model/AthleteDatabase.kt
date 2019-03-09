package com.example.calculaterepmax.Model

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.example.calculaterepmax.AsyncTask.PopulateDbAsyncTask

@Database(entities = [Athlete::class], version = 3)
abstract class AthleteDatabase: RoomDatabase() {

    abstract fun athleteDao(): AthleteDao

    companion object {
        private var instance : AthleteDatabase? = null
        fun getInstance(context: Context): AthleteDatabase? {
            if(instance == null){
                synchronized(AthleteDatabase::class){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AthleteDatabase::class.java, "athletes_database")
                        .fallbackToDestructiveMigration()
                        .addCallback(object : RoomDatabase.Callback(){
                            override fun onCreate(db: SupportSQLiteDatabase) {
                                super.onCreate(db)
                                PopulateDbAsyncTask(instance).execute(context)
                            }
                        })
                        .build()
                }
            }
            return instance
        }

        fun destroyInstance(){
            instance = null
        }
    }
}