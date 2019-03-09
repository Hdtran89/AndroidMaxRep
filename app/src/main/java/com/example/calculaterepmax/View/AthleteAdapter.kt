package com.example.calculaterepmax.View

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import com.example.calculaterepmax.Model.Athlete
import com.example.calculaterepmax.R
import java.util.*

class AthleteAdapter: RecyclerView.Adapter<AthleteAdapter.AthleteHolder>() {
    private var athletes: List<Athlete> = ArrayList()
    var onItemClick: ((Athlete) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AthleteHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.athlete_item, parent, false)
        return AthleteHolder(itemView)
    }

    override fun onBindViewHolder(holder: AthleteHolder, position: Int) {
        val currentAthlete = athletes[position]
        holder.workoutNameText.text = currentAthlete.exerciseName
        holder.workoutUnitText.text = currentAthlete.oneRepMax.toString()

    }

    override fun getItemCount(): Int {
        return athletes.size
    }

    fun setAthletes(athletes : List<Athlete>){
        this.athletes = athletes
        notifyDataSetChanged()
    }

    inner class AthleteHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var workoutNameText: TextView = itemView.findViewById(R.id.workout_name_text)
        var workoutUnitText: TextView = itemView.findViewById(R.id.workout_unit_text)

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(athletes[adapterPosition])
            }
        }
    }
}