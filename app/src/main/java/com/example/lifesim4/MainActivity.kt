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
import androidx.databinding.DataBindingUtil
import com.example.lifesim4.databinding.ActivityMainBinding
import com.example.lifesim4.models.GameEngine
import com.example.lifesim4.models.Person
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity()  {

    private lateinit var gameEngine: GameEngine
    private lateinit var player: Person
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setContentView(binding.root)

        gameEngine = GameEngine.getInstance().apply { startGame() }
        player = gameEngine.getPlayer()
        binding.person = player
       // binding.statusBar.person = player

        val myContract = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                simulateUI()
                if (data != null) {
                    val job = data.getStringExtra("Job")
                    val partTime = data.getStringExtra("Part Time")
                    if (job != null) {
                        Log.d("Debug", "Job: $job")
                        addTextViewToEvents(job)
                    }
                    if (partTime != null) {
                        addTextViewToEvents(partTime)
                    }
                }
            }
        }

        val bottomNavBar = findViewById<BottomNavigationView>(R.id.bottomNavBar)
        bottomNavBar.setOnItemSelectedListener  { item ->
            when (item.itemId) {
                R.id.Job -> {
                    // Handle Home menu item click
                    // Add your code here
                    //startActivity(Intent(this, JobActivity::class.java))
                    val intent = Intent(this, JobActivity::class.java)
                    myContract.launch(intent)
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
                    simulateUI()
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

    fun simulateUI() {
        changestatusUI()
       addTextViewToEvents("Age: ${player.age}")
    }

    fun addTextViewToEvents(text: String){
        val scrollView = binding.scrollView
        val eventLayout = binding.eventLayout

        val textView = TextView(this)
        textView.text = text // Set the text for the TextView
        textView.setTextColor(Color.BLUE) // Set the text color
        textView.setTextSize(16F)
        textView.setTypeface(null, Typeface.BOLD)

        // Add the TextView to the LinearLayout
        eventLayout.addView(textView)

        scrollView.post {
            scrollView.fullScroll(ScrollView.FOCUS_DOWN)
        }
    }

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
