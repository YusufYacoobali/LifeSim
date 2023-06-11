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

        //send data back that you started a new job
        partTimeJob.setOnClickListener {
            gameEngine.sendMessage("New Part time Job, You are now a swimming instructor")
            setResult(Activity.RESULT_OK)
            finish()

        }
    }
}