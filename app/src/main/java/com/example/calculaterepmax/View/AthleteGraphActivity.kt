package com.example.calculaterepmax.View

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.data.Set
import com.example.calculaterepmax.Model.Athlete
import com.example.calculaterepmax.R
import com.example.calculaterepmax.ViewModel.AthleteViewModel
import java.util.ArrayList
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.enums.MarkerType
import com.anychart.enums.Anchor
import com.anychart.enums.TooltipPositionMode
import com.anychart.graphics.vector.Stroke

class AthleteGraphActivity: AppCompatActivity() {

    private lateinit var workoutText : TextView
    private lateinit var anyChartView : AnyChartView
    private lateinit var workoutUnit : TextView
    private val ATHLETE_INTENT = "athlete_intent"
    private lateinit var athleteViewModel: AthleteViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_athlete_graph)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        val athlete = intent.extras.getSerializable(ATHLETE_INTENT) as? Athlete
        workoutText = findViewById(R.id.workout_name_text)
        workoutUnit = findViewById(R.id.workout_unit_text)
        anyChartView = findViewById(R.id.any_chart_view)

        workoutText.text = athlete?.exerciseName
        workoutUnit.text = athlete?.oneRepMax.toString()

        var listAthleteData = ArrayList<DataEntry>()
        athleteViewModel = ViewModelProviders.of(this).get(AthleteViewModel::class.java)
        athleteViewModel.getAllAthletes()
            .observe(this,
                Observer<List<Athlete>> {
                    val sortList = it?.filter { it.exerciseName == athlete?.exerciseName }.orEmpty().toMutableList()
                    sortList.sortBy { it.oneRepMax }
                    sortList.forEach {
                        listAthleteData.add(AthleteDataEntry(it.dateOfWorkout, it.oneRepMax))
                    }
                    val cartesian = AnyChart.line()
                    cartesian.animation(true)
                    cartesian.crosshair().enabled(true)
                    cartesian.crosshair()
                        .yLabel(true)
                        .yStroke(null as Stroke?, null, null, null as String?, null as String?)

                    cartesian.tooltip().positionMode(TooltipPositionMode.POINT)
                    val set = Set.instantiate()
                    set.data(listAthleteData)
                    val series1Mapping = set.mapAs("{ x: 'x', value: 'value' }")
                    val series1 = cartesian.line(series1Mapping)
                    series1.name("One Rep Max")
                    series1.hovered().markers().enabled(true)
                    series1.hovered().markers()
                        .type(MarkerType.CIRCLE)
                        .size(4.0)
                    series1.tooltip()
                        .position("right")
                        .anchor(Anchor.LEFT_CENTER)
                        .offsetX(5.0)
                        .offsetY(5.0)

                    anyChartView.setChart(cartesian)

            })
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
    private inner class AthleteDataEntry internal constructor(x: String, value: Int) : ValueDataEntry(x, value)
}