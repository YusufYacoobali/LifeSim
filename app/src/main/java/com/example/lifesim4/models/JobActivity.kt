package com.example.lifesim4.models

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import com.example.lifesim4.R

class JobActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_job)

        val fullTimeJobLayout: LinearLayout = findViewById(R.id.fullTimeJobLayout)
        fullTimeJobLayout.setOnClickListener {
            Toast.makeText(this, "Full-time job clicked", Toast.LENGTH_SHORT).show()
            // Handle the click event for full-time job layout
            // Navigate to another page or perform any desired action
        }

        val partTimeJobLayout: LinearLayout = findViewById(R.id.partTimeJobLayout)
        partTimeJobLayout.setOnClickListener {
            Toast.makeText(this, "Part-time job clicked", Toast.LENGTH_SHORT).show()
            // Handle the click event for part-time job layout
            // Navigate to another page or perform any desired action
        }

        val entrepreneurLayout: LinearLayout = findViewById(R.id.entrepreneurLayout)
        entrepreneurLayout.setOnClickListener {
            Toast.makeText(this, "Entrepreneur clicked", Toast.LENGTH_SHORT).show()
            // Handle the click event for entrepreneur layout
            // Navigate to another page or perform any desired action
        }

        val governmentLayout: LinearLayout = findViewById(R.id.governmentLayout)
        governmentLayout.setOnClickListener {
            Toast.makeText(this, "Government clicked", Toast.LENGTH_SHORT).show()
            // Handle the click event for government layout
            // Navigate to another page or perform any desired action
        }

        val criminalLayout: LinearLayout = findViewById(R.id.criminalLayout)
        criminalLayout.setOnClickListener {
            Toast.makeText(this, "Criminal clicked", Toast.LENGTH_SHORT).show()
            // Handle the click event for criminal layout
            // Navigate to another page or perform any desired action
        }

    }
}