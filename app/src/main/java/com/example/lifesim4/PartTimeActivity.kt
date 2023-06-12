package com.example.lifesim4

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import com.example.lifesim4.models.GameEngine

class PartTimeActivity : AppCompatActivity() {

    private lateinit var gameEngine: GameEngine
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_part_time)
        gameEngine = GameEngine.getInstance()

        val partTime1: LinearLayout = findViewById(R.id.partTime1)
        val partTime2: LinearLayout = findViewById(R.id.partTime2)

        val clickListener = View.OnClickListener { view ->
            when (view.id) {
                R.id.partTime1 -> {
                    gameEngine.sendMessage("You started working part time as a Life Guard with a salary of $13/hour")
                }
                R.id.partTime2 -> {
                    gameEngine.sendMessage("You started working part time as a Window Cleaner with a salary of $15/hour")
                }
            }

            setResult(Activity.RESULT_OK)
            finish()
        }

        partTime1.setOnClickListener(clickListener)
        partTime2.setOnClickListener(clickListener)

    }
}