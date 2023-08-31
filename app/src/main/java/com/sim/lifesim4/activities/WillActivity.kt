package com.sim.lifesim4.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import com.example.lifesim4.R

class WillActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_will)

        val changeWill: LinearLayout = findViewById(R.id.changeWill)
//        changeWill.setOnClickListener(
//        Toast.makeText(this@MainActivity, "Feature coming soon!", Toast.LENGTH_SHORT).show())
    }
}