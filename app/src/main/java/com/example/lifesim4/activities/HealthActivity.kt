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
                    val  (healthChange, charge) = player.doctorOptions(1)
                    if (charge == -1L){
                        gameEngine.sendMessage(Message("Minimum charge is $80k. You are broke and cannot afford this...lol", false))
                    } else {
                        gameEngine.sendMessage(Message(
                            "You visited the best doctor in the country.\nHealth ${if (healthChange >= 0) "+$healthChange" else healthChange} costing you\n ${
                                Tools.formatMoney(
                                    charge
                                )
                            }", false
                        ))
                    }
                }
                R.id.cheapDoctor -> {
                    val  (healthChange, charge) = player.doctorOptions(2)
                    if (charge == -1L){
                        gameEngine.sendMessage(Message("Minimum charge is $10k. You are broke and cannot afford this...lol", false))
                    } else {
                        gameEngine.sendMessage(Message(
                            "You visited any doctor you could find.\nHealth ${if (healthChange >= 0) "+$healthChange" else healthChange} costing you\n ${
                                Tools.formatMoney(
                                    charge
                                )
                            }", false
                        ))
                    }
                }
                R.id.witch -> {
                    val  (healthChange, charge) = player.doctorOptions(3)
                    if (charge == -1L){
                        gameEngine.sendMessage(Message("Minimum charge is $400. You can't even afford witches...lol", false))
                    } else {
                        gameEngine.sendMessage(Message(
                            "You visited some dodgy witches.\nHealth ${if (healthChange >= 0) "+$healthChange" else healthChange} costing you\n ${
                                Tools.formatMoney(
                                    charge
                                )
                            }", false
                        ))
                    }
                }
                R.id.potion -> {
                    val  (healthChange, charge) = player.doctorOptions(4)
                    gameEngine.sendMessage(Message("You cooked up a secret potion in your basement\nHealth ${if (healthChange >= 0) "+$healthChange" else healthChange}", false))
                }
                R.id.surgery -> {
                    val  (charmChange, charge) = player.doctorOptions(5)
                    if (charge == -1L){
                        gameEngine.sendMessage(Message("Minimum charge is $20k. You are broke and cannot afford this...lol", false))
                    } else {
                        gameEngine.sendMessage(Message(
                            "You visited a plastic surgeon to get work done.\nCharm ${if (charmChange >= 0) "+$charmChange" else charmChange} costing you\n ${
                                Tools.formatMoney(
                                    charge
                                )
                            }", false
                        ))
                    }
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