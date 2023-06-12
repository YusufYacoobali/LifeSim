package com.example.lifesim4

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.lifesim4.models.GameEngine

class HealthActivity : AppCompatActivity() {
    private lateinit var gameEngine: GameEngine

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_health)
        gameEngine = GameEngine.getInstance()
    }
}