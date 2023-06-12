package com.example.lifesim4

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import com.example.lifesim4.models.GameEngine

class ActivitiesActivity : AppCompatActivity() {

    private lateinit var gameEngine: GameEngine
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_activities)
        gameEngine = GameEngine.getInstance()

        val myContract = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
               setResult(Activity.RESULT_OK)
                finish()
            }
        }

        val fitnessButton: LinearLayout = findViewById(R.id.fitnessActivity)
        val clickListener = View.OnClickListener { view ->
            when (view.id) {
                R.id.fitnessActivity -> {
                    val intent = Intent(this, FitnessActivity::class.java)
                    myContract.launch(intent)
                }
            }
        }

        fitnessButton.setOnClickListener(clickListener)
    }
}