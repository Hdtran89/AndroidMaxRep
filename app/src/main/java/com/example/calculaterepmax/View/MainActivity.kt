package com.example.calculaterepmax.View

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.calculaterepmax.Model.Athlete
import com.example.calculaterepmax.R
import com.example.calculaterepmax.ViewModel.AthleteViewModel
import java.io.Serializable
import android.util.TypedValue
import android.view.View

class MainActivity : AppCompatActivity() {

    private lateinit var athleteViewModel: AthleteViewModel
    private lateinit var recyclerView: RecyclerView
    private val adapter = AthleteAdapter()
    private val ADD_ATHLETE_REQUEST = 1
    private val GRAPH_ATHLETE_REQUEST = 2
    private val ATHLETE_INTENT = "athlete_intent"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp)
        recyclerView = findViewById(R.id.recycler_view)
        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener { view ->
            startActivityForResult(
                Intent(this, AthleteFileActivity::class.java),
                ADD_ATHLETE_REQUEST
            )
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        val itemDecoration = SeparatorDecoration(this, Color.WHITE, 1.5f)
        recyclerView.addItemDecoration(itemDecoration)
        recyclerView.adapter = adapter
        athleteViewModel = ViewModelProviders.of(this).get(AthleteViewModel::class.java)
        athleteViewModel.getAllAthletes().observe(this,
            Observer<List<Athlete>> {
                val sortedList = it!!.sortedWith(compareBy({ it.exerciseName },{it.oneRepMax}))
                val oneRepMaxList = mutableListOf<Athlete>()
                var athleteMap = mutableMapOf<String, List<Athlete>>()

                for (i in 0 until sortedList.size - 1){
                    val currentItem = sortedList[i]
                    var nextItem : Athlete?

                    //Check for bounds of array
                    if(i < sortedList.size - 1){
                        nextItem = sortedList[i+1]
                    } else {
                        nextItem = currentItem
                    }
                    //Compare ExcerciseName of Current with Next
                    if(currentItem.exerciseName != nextItem.exerciseName){
                        val newList = sortedList.filter { nextItem.exerciseName == it.exerciseName }
                        athleteMap.put(nextItem.exerciseName, newList)
                    } else {
                        val newList = sortedList.filter { currentItem.exerciseName == it.exerciseName }
                        athleteMap.put(currentItem.exerciseName, newList)
                    }
                }
                for(item in athleteMap){
                    oneRepMaxList.add(item.value.maxBy { it.oneRepMax }!!)
                }
                adapter.setAthletes(oneRepMaxList)
            })
        adapter.onItemClick = {
            athlete ->
            val intent = Intent(this, AthleteGraphActivity::class.java).apply {
                putExtra(ATHLETE_INTENT, athlete as Serializable)
            }
            startActivityForResult(intent,GRAPH_ATHLETE_REQUEST)
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
                adapter.notifyDataSetChanged()
                Toast.makeText(this, "All athlete deleted!", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    class SeparatorDecoration(context: Context, color: Int, heightDp: Float) : RecyclerView.ItemDecoration() {
        private var mPaint : Paint
        init{
            mPaint = Paint()
            mPaint.color = color
            val thickness = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                heightDp, context.getResources().getDisplayMetrics())
            mPaint.setStrokeWidth(thickness)
        }

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            super.getItemOffsets(outRect, view, parent, state)
            val params = view.layoutParams as RecyclerView.LayoutParams

            // we want to retrieve the position in the list
            val position = params.viewAdapterPosition

            // and add a separator to any view but the last one
            if (position < state.itemCount) {
                outRect.set(0, 0, 0, mPaint.strokeWidth.toInt()) // left, top, right, bottom
            } else {
                outRect.setEmpty() // 0, 0, 0, 0
            }
        }

        override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
            super.onDraw(c, parent, state)
            val offset = (mPaint.strokeWidth / 2).toInt()

            // this will iterate over every visible view
            for (i in 0 until parent.childCount) {
                // get the view
                val view = parent.getChildAt(i)
                val params =  view.layoutParams as RecyclerView.LayoutParams
                val position = params.getViewAdapterPosition()

                // and finally draw the separator
                if (position < state.itemCount) {
                    c.drawLine(view.left.toFloat(), view.bottom.toFloat() + offset, view.right.toFloat(), view.bottom.toFloat() + offset, mPaint)
                }
            }
        }
    }
}
