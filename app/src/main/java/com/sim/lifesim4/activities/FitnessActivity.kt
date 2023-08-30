package com.sim.lifesim4.activities

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import com.example.lifesim4.R
import com.sim.lifesim4.models.GameEngine
import com.sim.lifesim4.models.GameEngine.*
import com.sim.lifesim4.models.Person

class FitnessActivity : AppCompatActivity() {

    private lateinit var gameEngine: GameEngine
    private lateinit var player: Person

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fitness)
        gameEngine = GameEngine.getInstance()
        player = gameEngine.getPlayer()

        //handle different buttons
        val gymOption: LinearLayout = findViewById(R.id.gymOption)
        val swimOption: LinearLayout = findViewById(R.id.swimOption)
        val runOption: LinearLayout = findViewById(R.id.runOption)
        val footballOption: LinearLayout = findViewById(R.id.footballOption)
        //add tennis, table tennis, sparring, build a house?

        val clickListener = View.OnClickListener { view ->
            when (view.id) {
                R.id.gymOption -> {
                    val  (charmChange, charge) = player.fitnessOptions(2)
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