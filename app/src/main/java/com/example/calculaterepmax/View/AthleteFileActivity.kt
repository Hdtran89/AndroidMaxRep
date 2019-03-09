package com.example.calculaterepmax.View

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.example.calculaterepmax.AsyncTask.ImportFileToDbAsyncTask
import com.example.calculaterepmax.R
import com.example.calculaterepmax.ViewModel.AthleteViewModel

class AthleteFileActivity: AppCompatActivity() {
    private lateinit var athleteViewModel: AthleteViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_athlete_file)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        val importButton = findViewById<Button>(R.id.import_button)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        val fileNameText = findViewById<EditText>(R.id.editTextFileName)
        progressBar.visibility = View.INVISIBLE
        importButton.setOnClickListener {
            val filename = fileNameText.text.toString()
            if(filename.isNotEmpty()){
                progressBar.visibility = View.VISIBLE
                athleteViewModel = ViewModelProviders.of(this).get(AthleteViewModel::class.java)
                ImportFileToDbAsyncTask(athleteViewModel, filename, progressBar, applicationContext, this).execute()
            }
        }

        fileNameText.setOnFocusChangeListener { v, hasFocus ->
            if(!hasFocus){
                hideKeyboard(v)
            } else {
                fileNameText.text.clear()
            }
        }
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.delete_all_records -> {
                athleteViewModel.deleteAllAthletes()
                Toast.makeText(this, "All athlete deleted!", Toast.LENGTH_SHORT).show()
                true
            }
            android.R.id.home -> {
                super.onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    fun hideKeyboard(view: View){
        val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}