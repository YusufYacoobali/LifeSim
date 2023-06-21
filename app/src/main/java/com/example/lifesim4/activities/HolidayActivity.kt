package com.example.lifesim4.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.lifesim4.R
import com.example.lifesim4.models.GameEngine

class HolidayActivity : AppCompatActivity() {
    private lateinit var gameEngine: GameEngine

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_holiday)
        gameEngine = GameEngine.getInstance()
    }
}