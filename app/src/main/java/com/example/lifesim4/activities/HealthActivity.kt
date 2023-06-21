package com.example.lifesim4.activities

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import com.example.lifesim4.R
import com.example.lifesim4.models.GameEngine

class HealthActivity : AppCompatActivity() {
    private lateinit var gameEngine: GameEngine

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_health)
        gameEngine = GameEngine.getInstance()

        val clickListener = View.OnClickListener { view ->
            when (view.id) {
                R.id.bestDoctor -> {
                    gameEngine.goDoctors(1)
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