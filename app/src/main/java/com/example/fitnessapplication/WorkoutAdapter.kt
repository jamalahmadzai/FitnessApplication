package com.example.fitnessapplication


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class WorkoutAdapter(private val exerciseSets : List<MainActivity.ExerciseSet>) : RecyclerView.Adapter<WorkoutAdapter.ItemViewHolder>() {
    inner class ItemViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val textView : TextView = view.findViewById(R.id.textView)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.workoutset_item, parent, false)
        return ItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return  exerciseSets.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val exerciseSet = exerciseSets[position]
        "${exerciseSet.workout} Set #${exerciseSet.setcount}: ${exerciseSet.reps} reps".also { holder.textView.text = it }

    }

}