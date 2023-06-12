package com.example.lifesim4

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts

class ActivitiesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_activities)

        val myContract = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
               setResult(Activity.RESULT_OK)
                finish()
            }
        }

        val fitnessButton: LinearLayout = findViewById(R.id.fitnessActivity)
        fitnessButton.setOnClickListener {
            val intent = Intent(this, FitnessActivity::class.java)
            myContract.launch(intent)
        }
    }
}