package com.example.lifesim4

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import com.example.lifesim4.models.GameEngine

class PartTimeActivity : AppCompatActivity() {

    private lateinit var gameEngine: GameEngine
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_part_time)
        gameEngine = GameEngine.getInstance()

        val partTimeJob : LinearLayout = findViewById(R.id.partTimeJobLayout)
        val partTime1 : LinearLayout = findViewById(R.id.partTime1)
        val partTime2 : LinearLayout = findViewById(R.id.partTime2)

        partTimeJob.setOnClickListener {
            gameEngine.sendMessage("New Part time Job, You are now a swimming instructor")
            setResult(Activity.RESULT_OK)
            finish()

        }
        partTime1.setOnClickListener {
            gameEngine.sendMessage("You started working part time as a Life Guard with a salary of $13/hour")
            setResult(Activity.RESULT_OK)
            finish()

        }
        partTime2.setOnClickListener {
            gameEngine.sendMessage("You started working part time as a Window Cleaner with a salary of $15/hour")
            setResult(Activity.RESULT_OK)
            finish()

        }
    }
}