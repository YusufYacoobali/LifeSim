package com.example.lifesim4

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.activity.result.contract.ActivityResultContracts
import com.example.lifesim4.activities.BoatsActivity
import com.example.lifesim4.activities.CarsActivity
import com.example.lifesim4.activities.CharityActivity
import com.example.lifesim4.activities.EmigrateActivity
import com.example.lifesim4.activities.FitnessActivity
import com.example.lifesim4.activities.HealthActivity
import com.example.lifesim4.activities.HolidayActivity
import com.example.lifesim4.activities.LoveActivity
import com.example.lifesim4.activities.PartyActivity
import com.example.lifesim4.activities.PlanesActivity
import com.example.lifesim4.activities.PropertiesActivity
import com.example.lifesim4.activities.SkillsActivity
import com.example.lifesim4.activities.WillActivity
import com.example.lifesim4.models.GameEngine

class ActivitiesActivity : AppCompatActivity() {

    private lateinit var gameEngine: GameEngine
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activities)
        gameEngine = GameEngine.getInstance()

        val myContract = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
               setResult(Activity.RESULT_OK)
                finish()
            }
        }

        val clickListener = View.OnClickListener { view ->
            when (view.id) {
                R.id.fitnessButton -> {
                    val intent = Intent(this, FitnessActivity::class.java)
                    myContract.launch(intent)
                }
                R.id.doctorButton -> {
                    val intent = Intent(this, HealthActivity::class.java)
                    myContract.launch(intent)
                }
                R.id.holidayButton -> {
                    val intent = Intent(this, HolidayActivity::class.java)
                    myContract.launch(intent)
                }
                R.id.loveButton -> {
                    val intent = Intent(this, LoveActivity::class.java)
                    myContract.launch(intent)
                }
                R.id.skillsButton -> {
                    val intent = Intent(this, SkillsActivity::class.java)
                    myContract.launch(intent)
                }
                R.id.housesButton -> {
                    val intent = Intent(this, PropertiesActivity::class.java)
                    myContract.launch(intent)
                }
                R.id.carsButton -> {
                    val intent = Intent(this, CarsActivity::class.java)
                    myContract.launch(intent)
                }
                R.id.boatsButton -> {
                    val intent = Intent(this, BoatsActivity::class.java)
                    myContract.launch(intent)
                }
                R.id.planesButton -> {
                    val intent = Intent(this, PlanesActivity::class.java)
                    myContract.launch(intent)
                }
                R.id.partyButton -> {
                    val intent = Intent(this, PartyActivity::class.java)
                    myContract.launch(intent)
                }
                R.id.charityButton -> {
                    val intent = Intent(this, CharityActivity::class.java)
                    myContract.launch(intent)
                }
                R.id.willButton -> {
                    val intent = Intent(this, WillActivity::class.java)
                    myContract.launch(intent)
                }
                R.id.emigrateButton -> {
                    val intent = Intent(this, EmigrateActivity::class.java)
                    myContract.launch(intent)
                }
                R.id.endLifeButton -> {
                    gameEngine.startGame()
                    val intent = Intent()
                    intent.putExtra("New", "1")
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                }
            }
        }

        val fitnessButton: LinearLayout = findViewById(R.id.fitnessButton)
        val endLifeButton: LinearLayout = findViewById(R.id.endLifeButton)
        val doctorButton: LinearLayout = findViewById(R.id.doctorButton)
        val holidayButton: LinearLayout = findViewById(R.id.holidayButton)
        val loveButton: LinearLayout = findViewById(R.id.loveButton)
        val skillsButton: LinearLayout = findViewById(R.id.skillsButton)
        val housesButton: LinearLayout = findViewById(R.id.housesButton)
        val carsButton: LinearLayout = findViewById(R.id.carsButton)
        val boatsButton: LinearLayout = findViewById(R.id.boatsButton)
        val planesButton: LinearLayout = findViewById(R.id.planesButton)
        val partyButton: LinearLayout = findViewById(R.id.partyButton)
        val charityButton: LinearLayout = findViewById(R.id.charityButton)
        val willButton: LinearLayout = findViewById(R.id.willButton)
        val emigrateButton: LinearLayout = findViewById(R.id.emigrateButton)

        fitnessButton.setOnClickListener(clickListener)
        endLifeButton.setOnClickListener(clickListener)
        doctorButton.setOnClickListener(clickListener)
        holidayButton.setOnClickListener(clickListener)
        loveButton.setOnClickListener(clickListener)
        skillsButton.setOnClickListener(clickListener)
        housesButton.setOnClickListener(clickListener)
        carsButton.setOnClickListener(clickListener)
        boatsButton.setOnClickListener(clickListener)
        planesButton.setOnClickListener(clickListener)
        partyButton.setOnClickListener(clickListener)
        charityButton.setOnClickListener(clickListener)
        willButton.setOnClickListener(clickListener)
        emigrateButton.setOnClickListener(clickListener)
    }
}