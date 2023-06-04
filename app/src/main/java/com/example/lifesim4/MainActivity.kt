package com.example.lifesim4

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.lifesim4.databinding.ActivityMainBinding
import com.example.lifesim4.databinding.EventsListBinding
import com.example.lifesim4.models.GameEngine
import com.example.lifesim4.models.Person

class MainActivity : AppCompatActivity() {

    private lateinit var gameEngine: GameEngine
    private lateinit var player: Person

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setContentView(binding.root)

        binding.bottomButtons.bottomNavBar.apply {
            background = null
            menu.getItem(2).isEnabled = false
        }

        gameEngine = GameEngine().apply { startGame() }
        player = gameEngine.getPlayer()
        binding.person = player
        binding.statusBar.person = player

        binding.bottomButtons.fab.setOnClickListener {
            gameEngine.simulate()
            simulateUI(binding)
        }
    }

    fun simulateUI(binding: ActivityMainBinding) {
        changestatusUI(binding)

        // Get a reference to the LinearLayout in the events_list layout
        val eventLayout = binding.eventsList.root.findViewById<LinearLayout>(R.id.eventLayout)

        val textView = TextView(this)
        textView.text = "Age: ${player.age} " // Set the text for the TextView
        textView.setTextColor(Color.BLUE) // Set the text color
        textView.setTextSize(16F)
        textView.setTypeface(null, Typeface.BOLD)

        // Add the TextView to the LinearLayout
        eventLayout.addView(textView)
    }

    private fun changestatusUI(binding: ActivityMainBinding) {
        binding.statusBar.ageText.text = player.age.toString()
        binding.statusBar.fameText.text = player.fame.name

        binding.statusBar.vitalityProgressText.text = player.health.toString()
        binding.statusBar.vitalityProgressBar.progress = player.health

        binding.statusBar.geniusProgressText.text = player.genius.toString()
        binding.statusBar.geniusProgressBar.progress = player.genius

        binding.statusBar.charmProgressText.text = player.charm.toString()
        binding.statusBar.charmProgressBar.progress = player.charm

        binding.statusBar.fortuneProgressText.text = player.fortune.toString()
        binding.statusBar.fortuneProgressBar.progress = player.fortune

        binding.statusBar.moneyText.text = player.money.toString()

    }
}
