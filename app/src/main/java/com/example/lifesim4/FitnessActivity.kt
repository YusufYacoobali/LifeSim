package com.example.lifesim4

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import com.example.lifesim4.models.GameEngine

class FitnessActivity : AppCompatActivity() {

    private lateinit var gameEngine: GameEngine

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fitness)
        gameEngine = GameEngine.getInstance()

        //handle different buttons
        val GymOption: TextView = findViewById(R.id.GymOption)
        GymOption.setOnClickListener {
            gameEngine.goGym()
            gameEngine.sendMessage("Joined Gym, +1 charm")
            setResult(Activity.RESULT_OK)
            finish()
        }
    }
}