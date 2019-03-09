package com.example.calculaterepmax.AsyncTask

import android.content.Context
import android.os.AsyncTask
import android.util.Log
import com.example.calculaterepmax.Model.Athlete
import com.example.calculaterepmax.Model.AthleteDatabase
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception

class PopulateDbAsyncTask(db: AthleteDatabase?) : AsyncTask<Context, Unit, Unit>(){
    private val athleteDao = db?.athleteDao()
    override fun doInBackground(vararg params: Context?) {
        if(android.os.Debug.isDebuggerConnected())
            android.os.Debug.waitForDebugger()
        val context = params[0]
        val res_id = context?.resources?.getIdentifier("workoutdata","raw",context.packageName)
        val inputStream = context?.resources?.openRawResource(res_id!!)
        val inputStreamReader = InputStreamReader(inputStream)
        val bufferReader = BufferedReader(inputStreamReader, 8192)
        val listString = arrayListOf<String>()
        try {
            while (true){
                val temp = bufferReader.readLine() ?: break
                listString.add(temp)
            }
            inputStream?.close()
            inputStreamReader.close()
            bufferReader.close()
        } catch (e: Exception){
            Log.e("Error", e.toString())
        }
        if(listString.isNotEmpty()){
            for(item in listString){
                val stringArray = item.split(',')
                val weight = stringArray[4]
                val reps = stringArray[3]
                val oneRepMax = findOneRepMax(weight.toInt(),reps.toInt())
                val athlete = Athlete(stringArray[0],stringArray[1],stringArray[2],stringArray[3], stringArray[4], oneRepMax)
                athleteDao?.insert(athlete)
            }
        }
    }
    private fun findOneRepMax(weight: Int, reps:Int) : Int{
        return weight * 36/(37-reps)
    }
}