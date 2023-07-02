package com.example.lifesim4.activities

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import com.example.lifesim4.R
import com.example.lifesim4.models.GameEngine
import com.example.lifesim4.models.GameEngine.*
import com.example.lifesim4.models.Person
import com.example.lifesim4.tools.Tools

class HealthActivity : AppCompatActivity() {
    private lateinit var gameEngine: GameEngine
    private lateinit var player: Person

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_health)
        gameEngine = GameEngine.getInstance()
        player = gameEngine.getPlayer()

        val clickListener = View.OnClickListener { view ->
            when (view.id) {
                R.id.bestDoctor -> {
                   // gameEngine.goDoctors(1)
                    //no need for game engine, use person method and send message from here
                    val  (healthChange, charge) = player.doctorOptions(1)
                    if (charge == -1L){
                        gameEngine.sendMessage(Message("Minimum charge is $80k. You are broke and cannot afford this...lol", false))
                    }
                    gameEngine.sendMessage(Message(
                        "You visited the best doctor in the country.\nHealth ${if (healthChange >= 0) "+$healthChange" else healthChange} costing you\n ${
                            Tools.formatMoney(
                                charge
                            )
                        }", false
                    ))
                }
                R.id.cheapDoctor -> {
                    gameEngine.goDoctors(2)
                }
                R.id.witch -> {
                    gameEngine.goDoctors(3)
                }
                R.id.potion -> {
                    gameEngine.goDoctors(4)
                }
                R.id.surgery -> {
                    gameEngine.goDoctors(5)
                }
            }
            setResult(Activity.RESULT_OK)
            finish()
        }

        val bestDoctor: LinearLayout = findViewById(R.id.bestDoctor)
        val cheapDoctor: LinearLayout = findViewById(R.id.cheapDoctor)
        val witch: LinearLayout = findViewById(R.id.witch)
        val potion: LinearLayout = findViewById(R.id.potion)
        val surgery: LinearLayout = findViewById(R.id.surgery)
        bestDoctor.setOnClickListener(clickListener)
        cheapDoctor.setOnClickListener(clickListener)
        witch.setOnClickListener(clickListener)
        potion.setOnClickListener(clickListener)
        surgery.setOnClickListener(clickListener)
    }
}