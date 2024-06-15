package com.example.fitnessapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import org.json.JSONArray
import java.io.IOException
import java.nio.charset.StandardCharsets
import android.content.Context
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


@Suppress("SameParameterValue")
class MainActivity : AppCompatActivity() {

    data class Exercise(
        val name: String,
        val force: String,
        val level: String,
        val mechanic: String,
        val equipment: String,
        val primaryMuscles: List<String>,
        val secondaryMuscles: List<String>,
        val instructions: List<String>,
        val category: String,
        val images: List<String>,
        val id: String
    )
    data class ExerciseSet(
        val workout: String,
        val reps : Int,
        val setcount: Int
    )

    private lateinit var adapter: WorkoutAdapter
    private val exerciseSets = mutableListOf<ExerciseSet>()
    private val exerciseSetCounts = mutableMapOf<String, Int>()


    private fun loadJSONFromAsset(context: Context, filename: String): String? {
        var json: String? = null
        try {
            val inputStream = context.assets.open(filename)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            json = String(buffer, StandardCharsets.UTF_8)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return json
    }
    private fun parseJSON(json: String?): List<String> {
        val names = mutableListOf<String>()
        try {
            val jsonArray = JSONArray(json)
            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                val name = jsonObject.getString("name")
                names.add(name)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return names
    }
    private fun updateWorkoutList() {
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = WorkoutAdapter(exerciseSets)
    }
    private fun onClick() {
        val exerciseAutoCompleteTextView = findViewById<AutoCompleteTextView>(R.id.exSearch)
        val repsAutoCompleteTextView = findViewById<AutoCompleteTextView>(R.id.repCount)
        val exercise = exerciseAutoCompleteTextView.text.toString()
        val repsString = repsAutoCompleteTextView.text.toString()
        val repsInt = repsString.toIntOrNull()

        if (exercise.isNotBlank() && repsInt!! > 0) {
            val setCount = (exerciseSetCounts[exercise] ?: 0) + 1
            exerciseSetCounts[exercise] = setCount
            exerciseSets.add(ExerciseSet(exercise, repsInt, setCount))
            updateWorkoutList()
        }
        else {
            Toast.makeText(this, "Invalid input!", Toast.LENGTH_SHORT).show()
        }

    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val exSearch = findViewById<AutoCompleteTextView>(R.id.exSearch)
        val json = loadJSONFromAsset(context = applicationContext, filename = "Exercises.json")
        Log.d("JSON", json ?: "JSON data is null")
        val exercises = parseJSON(json)
        Log.d("Exercise Names", exercises.toString())
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, exercises)
        exSearch.setAdapter(adapter)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        this.adapter = WorkoutAdapter(exerciseSets)
        recyclerView.adapter = this.adapter
        val button: Button = findViewById(R.id.button)
        button.setOnClickListener { onClick()}

        }
    

}