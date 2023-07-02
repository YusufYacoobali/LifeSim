package com.example.lifesim4.activities

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import com.example.lifesim4.R
import com.example.lifesim4.models.GameEngine
import com.example.lifesim4.models.GameEngine.*

class FitnessActivity : AppCompatActivity() {

    private lateinit var gameEngine: GameEngine

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fitness)
        gameEngine = GameEngine.getInstance()

        //handle different buttons
        val gymOption: LinearLayout = findViewById(R.id.gymOption)
        val swimOption: LinearLayout = findViewById(R.id.swimOption)

        val clickListener = View.OnClickListener { view ->
            when (view.id) {
                R.id.gymOption -> {
                    gameEngine.goGym()
                    gameEngine.sendMessage(Message("Joined Gym, +1 charm", false))
                }
                R.id.swimOption -> {
                    gameEngine.sendMessage(Message("You started swimming", false))
                }
            }
            setResult(Activity.RESULT_OK)
            finish()
        }

        gymOption.setOnClickListener(clickListener)
        swimOption.setOnClickListener(clickListener)
    }
}