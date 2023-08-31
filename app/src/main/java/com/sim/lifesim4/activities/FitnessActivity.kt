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
import com.sim.lifesim4.tools.Tools

class FitnessActivity : AppCompatActivity() {
    private lateinit var gameEngine: GameEngine
    private lateinit var player: Person

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fitness)
        gameEngine = GameEngine.getInstance()
        player = gameEngine.getPlayer()

        val clickListener = View.OnClickListener {
                val  (vitalityChange, charmChange, charge) = player.fitnessOptions()
                if (charge == -1L){
                    gameEngine.sendMessage(Message("That costs $10. You cannot afford this", false))
                } else {
                    gameEngine.sendMessage(Message(
                        "You had a great fitness session!\n Vitality: +${vitalityChange}\nCharm: +${charmChange} ", false
                    ))
                }
            setResult(Activity.RESULT_OK)
            finish()
        }

        val gymOption: LinearLayout = findViewById(R.id.gymOption)
        val swimOption: LinearLayout = findViewById(R.id.swimOption)
        val runOption: LinearLayout = findViewById(R.id.runOption)
        val footballOption: LinearLayout = findViewById(R.id.footballOption)
        //add tennis, table tennis, sparring, option to do regular activities with cost per year, build a house?
        gymOption.setOnClickListener(clickListener)
        swimOption.setOnClickListener(clickListener)
        runOption.setOnClickListener(clickListener)
        footballOption.setOnClickListener(clickListener)
    }
}