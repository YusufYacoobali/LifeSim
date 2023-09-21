package com.sim.lifesim4.jobs

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import com.example.lifesim4.R

class EntrepreneurActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.job_entrepreneur)

        val startBusiness: LinearLayout = findViewById(R.id.startBusiness)
        startBusiness.setOnClickListener {
            Toast.makeText(this@EntrepreneurActivity, "Feature coming soon!", Toast.LENGTH_SHORT).show()
        }
    }
}