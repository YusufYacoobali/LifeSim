package com.example.lifesim4

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.lifesim4.R
import com.example.lifesim4.databinding.ActivityMainBinding
import com.example.lifesim4.models.GameEngine

class JobActivity : AppCompatActivity()  {

    private lateinit var gameEngine: GameEngine
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_job)

        gameEngine = GameEngine.getInstance()

        val myContract = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                val receivedData = data?.getStringExtra("Part Time")
                val intent = Intent().apply {
                    putExtra("Part Time", receivedData)
                }
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }


        val fullTimeJobLayout: LinearLayout = findViewById(R.id.fullTimeJobLayout)
        fullTimeJobLayout.setOnClickListener {

            gameEngine.simulate()
            val resultIntent = Intent()
            resultIntent.putExtra("Job", "New Job, You are now a Police officer")
            setResult(Activity.RESULT_OK, resultIntent)
            finish()

        }

        val partTimeJobLayout: LinearLayout = findViewById(R.id.partTimeJobLayout)
        partTimeJobLayout.setOnClickListener {
            val intent = Intent(this, PartTimeActivity::class.java)
            myContract.launch(intent)
            true
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

