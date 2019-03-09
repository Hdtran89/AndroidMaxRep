package com.example.calculaterepmax.AsyncTask

import android.app.Activity
import android.content.Context
import android.os.AsyncTask
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import com.example.calculaterepmax.Model.Athlete
import com.example.calculaterepmax.ViewModel.AthleteViewModel
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception

class ImportFileToDbAsyncTask(val athleteViewModel: AthleteViewModel,
                              val fileName: String,
                              val progressBar: ProgressBar,
                              val context: Context,
                              val activity: Activity) : AsyncTask<Unit, Unit, Unit>(){
    override fun doInBackground(vararg params: Unit?) {
        if(android.os.Debug.isDebuggerConnected())
            android.os.Debug.waitForDebugger()
        val res_id = context.resources.getIdentifier(fileName,"raw",context.packageName)
        val inputStream = context.resources.openRawResource(res_id)
        val inputStreamReader = InputStreamReader(inputStream)
        val bufferReader = BufferedReader(inputStreamReader, 8192)
        val listString = arrayListOf<String>()
        try {
            while (true){
                val temp = bufferReader.readLine() ?: break
                listString.add(temp)
            }
            inputStream.close()
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
                athleteViewModel.insert(athlete)
            }
        }
    }

    private fun findOneRepMax(weight: Int, reps:Int) : Int{
        return weight * 36/(37-reps)
    }

    override fun onPostExecute(result: Unit?) {
        super.onPostExecute(result)
        progressBar.visibility = View.INVISIBLE
        activity.setResult(Activity.RESULT_OK)
        activity.finish()
    }
}