package com.example.lifesim4

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.lifesim4.databinding.ActivityMainBinding
import com.example.lifesim4.models.GameEngine
import com.example.lifesim4.models.Person
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var gameEngine: GameEngine
    private lateinit var player: Person

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setContentView(binding.root)

        gameEngine = GameEngine().apply { startGame() }
        player = gameEngine.getPlayer()
        binding.person = player
       // binding.statusBar.person = player

        val bottomNavBar = findViewById<BottomNavigationView>(R.id.bottomNavBar)
        bottomNavBar.setOnItemSelectedListener  { item ->
            when (item.itemId) {
                R.id.Job -> {
                    // Handle Home menu item click
                    // Add your code here
                    startActivity(Intent(this, JobActivity::class.java))
                    true
                }
                R.id.Assets -> {
                    // Handle Search menu item click
                    // Add your code here
                    true
                }
                R.id.Age -> {
                    // Handle Placeholder menu item click
                    // Add your code here
                    gameEngine.simulate()
                    simulateUI(binding)
                    true
                }
                R.id.Relations -> {
                    // Handle Profile menu item click
                    // Add your code here
                    true
                }
                R.id.Personal -> {
                    // Handle Settings menu item click
                    // Add your code here
                    true
                }
                else -> false
            }
        }
    }

    fun simulateUI(binding: ActivityMainBinding) {
        changestatusUI(binding)

        val scrollView = binding.scrollView
        val eventLayout = binding.eventLayout

        val textView = TextView(this)
        textView.text = "Age: ${player.age} " // Set the text for the TextView
        textView.setTextColor(Color.BLUE) // Set the text color
        textView.setTextSize(16F)
        textView.setTypeface(null, Typeface.BOLD)

        // Add the TextView to the LinearLayout
        eventLayout.addView(textView)

        scrollView.post {
            scrollView.fullScroll(ScrollView.FOCUS_DOWN)
        }
    }

    private fun changestatusUI(binding: ActivityMainBinding) {
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
