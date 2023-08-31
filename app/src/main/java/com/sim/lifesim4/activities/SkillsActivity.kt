package com.sim.lifesim4.activities

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.widget.SwitchCompat
import com.example.lifesim4.R
import com.sim.lifesim4.models.GameEngine
import com.sim.lifesim4.models.Person
import com.sim.lifesim4.tools.Tools

class SkillsActivity : AppCompatActivity() {
    private lateinit var gameEngine: GameEngine
    private lateinit var player: Person
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_skills)
        gameEngine = GameEngine.getInstance()
        player = gameEngine.getPlayer()

        //set each switch based on shared prefs
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val readSwitch: SwitchCompat = findViewById(R.id.readSwitch)
        readSwitch.isChecked = sharedPreferences.getBoolean("readSwitch", false)
        val actingSwitch: SwitchCompat = findViewById(R.id.actingSwitch)
        actingSwitch.isChecked = sharedPreferences.getBoolean("actingSwitch", false)
        val praySwitch: SwitchCompat = findViewById(R.id.praySwitch)
        praySwitch.isChecked = sharedPreferences.getBoolean("praySwitch", false)
        val fightSwitch: SwitchCompat = findViewById(R.id.fightSwitch)
        fightSwitch.isChecked = sharedPreferences.getBoolean("fightSwitch", false)
        val politicsSwitch: SwitchCompat = findViewById(R.id.politicsSwitch)
        politicsSwitch.isChecked = sharedPreferences.getBoolean("politicsSwitch", false)
        val crimeSwitch: SwitchCompat = findViewById(R.id.crimeSwitch)
        crimeSwitch.isChecked = sharedPreferences.getBoolean("crimeSwitch", false)

        // Initialize, load, and save states for multiple switches
        setupSwitch(readSwitch, "readSwitch")
        setupSwitch(actingSwitch, "actingSwitch")
        setupSwitch(praySwitch, "praySwitch")
        setupSwitch(fightSwitch, "fightSwitch")
        setupSwitch(politicsSwitch, "politicsSwitch")
        setupSwitch(crimeSwitch, "crimeSwitch")
    }

    private fun setupSwitch(switch: SwitchCompat, key: String) {
        // Load and set the state from SharedPreferences
        switch.isChecked = sharedPreferences.getBoolean(key, false)

        switch.setOnClickListener {
            // Save the updated state to SharedPreferences
            sharedPreferences.edit().putBoolean(key, switch.isChecked).apply()

            // Update the activity result and finish the activity
            setResult(Activity.RESULT_OK)
            //finish()
        }
    }

    fun sendMessage(charge: Long, successMessage: String) {
        if (charge == -1L){
            gameEngine.sendMessage(
                GameEngine.Message(
                    "You can't afford to move at this moment",
                    false
                )
            )
        } else {
            gameEngine.sendMessage(
                GameEngine.Message(
                    successMessage, false
                )
            )
        }
    }
}