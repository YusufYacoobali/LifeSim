package com.example.lifesim4

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.widget.ScrollView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.lifesim4.databinding.ActivityMainBinding
import com.example.lifesim4.models.GameEngine
import com.example.lifesim4.models.Person
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity()  {

    private lateinit var gameEngine: GameEngine
    private lateinit var binding: ActivityMainBinding
    private lateinit var player: Person

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setContentView(binding.root)
        gameEngine = GameEngine.getInstance().apply { startGame() }
        player = gameEngine.getPlayer()

        startLife()

        //handle data when user comes back to main page
        val myContract = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                changestatusUI()
                printAllMessages()
            }
        }

        //bottom nav bar button navigation
        val bottomNavBar = findViewById<BottomNavigationView>(R.id.bottomNavBar)
        bottomNavBar.setOnItemSelectedListener { item ->
            val intent: Intent? = when (item.itemId) {
                R.id.Job -> Intent(this, JobActivity::class.java)
                R.id.Assets -> Intent(this, AssetActivity::class.java)
                R.id.Age -> {
                    gameEngine.simulate()
                    simulateUI()
                    null
                }
                R.id.Relations -> Intent(this, RelationsActivity::class.java)
                R.id.Personal -> Intent(this, ActivitiesActivity::class.java)
                else -> null
            }
            intent?.let {
                myContract.launch(it)
            }
            intent != null
        }
    }

    //Start new Life
    private fun startLife(){
        binding.playerName.text = player.name
        addAgeTextViewToEvents()
        addTextViewToEvents("You are born as a ${player.gender}")
        addTextViewToEvents("Your name is ${player.name}")
        changestatusUI()
    }

    private fun printAllMessages(){
        val messages = gameEngine.getAllMessages()
        for (message in messages){
            addTextViewToEvents(message)
        }
    }

    //Used for Age button
    private fun simulateUI() {
        changestatusUI()
        addAgeTextViewToEvents()
    }

    //Used by events from other pages
    private fun addTextViewToEvents(text: String){
        val textView = TextView(this)
        textView.text = text // Set the text for the TextView
        textView.setTextColor(ContextCompat.getColor(this, R.color.eventText))
        textView.textSize = 15F
        textView.setPadding(0,0,0,7)
        textView.setTypeface(null, Typeface.NORMAL)

        // Add the TextView to the LinearLayout
        binding.eventLayout.addView(textView)
        val scrollView = binding.scrollView
        scrollView.post {
            scrollView.fullScroll(ScrollView.FOCUS_DOWN)
        }
    }

    //Different Age text style
    private fun addAgeTextViewToEvents(){
        val textView = TextView(this)
        val text = "Age: ${player.age} years"
        textView.text = text
        textView.setTextColor(ContextCompat.getColor(this, R.color.ageText))
        textView.textSize = 17F
        textView.setPadding(0,20,0,10)
        textView.setTypeface(null, Typeface.BOLD)

        // Add the TextView to the LinearLayout
        binding.eventLayout.addView(textView)
        val scrollView = binding.scrollView
        scrollView.post {
            scrollView.fullScroll(ScrollView.FOCUS_DOWN)
        }
    }

    //Update status bar
    private fun changestatusUI() {
        binding.ageText.text = player.age.toString()
        binding.fameText.text = player.fame.name

        binding.vitalityProgressText.text = player.health.toString()
        binding.vitalityProgressBar.progress = player.health

        binding.geniusProgressText.text = player.genius.toString()
        binding.geniusProgressBar.progress = player.genius

        binding.charmProgressText.text = player.charm.toString()
        binding.charmProgressBar.progress = player.charm

        binding.fortuneProgressText.text = player.fortune.toString()
        binding.fortuneProgressBar.progress = player.fortune

        binding.moneyText.text = player.money.toString()
    }
}
