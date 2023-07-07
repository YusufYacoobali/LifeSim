package com.example.lifesim4

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.lifesim4.assets.AssetActivity
import com.example.lifesim4.jobs.FullTimeActivity
import com.example.lifesim4.jobs.PartTimeActivity
import com.example.lifesim4.models.GameEngine
import com.example.lifesim4.models.Person
import com.example.lifesim4.tools.Tools

class JobActivity : AppCompatActivity()  {

    private lateinit var gameEngine: GameEngine
    private lateinit var player: Person
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_job)
        gameEngine = GameEngine.getInstance()
        player = gameEngine.getPlayer()

        //Get data from next screen and pass it to main screen
        val myContract = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                setResult(Activity.RESULT_OK)
                finish()
            }
        }

        val currentWorkLayout: LinearLayout = findViewById(R.id.current)

        if (player.job != null) {
            Tools.addCardToView(this, player.job,  currentWorkLayout, player.job!!.type.toString(), R.drawable.home, null, myContract)
        }

        //handle different buttons
        val fullTimeJobLayout: LinearLayout = findViewById(R.id.fullTimeJobLayout)
        fullTimeJobLayout.setOnClickListener {
            val intent = Intent(this, FullTimeActivity::class.java)
            myContract.launch(intent)
        }

        val partTimeJobLayout: LinearLayout = findViewById(R.id.partTimeJobLayout)
        partTimeJobLayout.setOnClickListener {
            val intent = Intent(this, PartTimeActivity::class.java)
            myContract.launch(intent)
        }

        val entrepreneurLayout: LinearLayout = findViewById(R.id.entrepreneurLayout)
        entrepreneurLayout.setOnClickListener {
            Toast.makeText(this, "Entrepreneur clicked", Toast.LENGTH_SHORT).show()
        }

        val governmentLayout: LinearLayout = findViewById(R.id.governmentLayout)
        governmentLayout.setOnClickListener {
            Toast.makeText(this, "Government clicked", Toast.LENGTH_SHORT).show()
        }

        val criminalLayout: LinearLayout = findViewById(R.id.criminalLayout)
        criminalLayout.setOnClickListener {
            Toast.makeText(this, "Criminal clicked", Toast.LENGTH_SHORT).show()
        }
    }
}

