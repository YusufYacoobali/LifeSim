package com.example.lifesim4

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
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
        val gymOption: LinearLayout = findViewById(R.id.gymOption)
        val swimOption: LinearLayout = findViewById(R.id.swimOption)

        val clickListener = View.OnClickListener { view ->
            when (view.id) {
                R.id.gymOption -> {
                    gameEngine.goGym()
                    gameEngine.sendMessage("Joined Gym, +1 charm")
                }
                R.id.swimOption -> {
                    gameEngine.sendMessage("You started swimming")
                }
            }
            setResult(Activity.RESULT_OK)
            finish()
        }

        gymOption.setOnClickListener(clickListener)
        swimOption.setOnClickListener(clickListener)
    }
}