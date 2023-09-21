package com.sim.lifesim4.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import com.example.lifesim4.R

class LawsuitActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lawsuit)

        val Father: LinearLayout = findViewById(R.id.Father)
        Father.setOnClickListener {
            Toast.makeText(this@LawsuitActivity, "Feature coming soon!", Toast.LENGTH_SHORT).show()
        }
    }
}