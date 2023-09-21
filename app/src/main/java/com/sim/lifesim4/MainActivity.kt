package com.sim.lifesim4

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.widget.ScrollView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.lifesim4.R
import com.example.lifesim4.databinding.MainMainBinding
import com.sim.lifesim4.models.GameEngine
import com.sim.lifesim4.models.Person
import com.sim.lifesim4.tools.Tools
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.sim.lifesim4.models.Job
import com.sim.lifesim4.models.Skill

class MainActivity : AppCompatActivity()  {

    private lateinit var gameEngine: GameEngine
    private lateinit var binding: MainMainBinding
    private lateinit var player: Person

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.main_main)
        setContentView(binding.root)

        val gameEngineData = GameEngine.loadGameEngineFromFile(this, "game_state.bin")
        if (gameEngineData != null) {
            gameEngine = gameEngineData
            gameEngine.setPlayer(gameEngineData.getPlayer())
            player = gameEngineData.getPlayer()
            printLoadAllMessages()
            println("Game Loaded")
            changestatusUI()
        } else {
            println("Game not loaded")
            gameEngine = GameEngine.getInstance()
            startNewGame()
        }

        //handle data when user comes back to main page
        val myContract = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                val newLife = data?.getStringExtra("New")

                if (newLife != null) {
                    startNewGame()
                }
                player.calcNetWorth()
                changestatusUI()
                printAllMessages()
                printLogMessages()
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
    private fun startNewGame(){
        deleteAllEvents()
        gameEngine.startGame()
        player = gameEngine.getPlayer()
        player.calcNetWorth()
        changestatusUI()
        resetSharedPrefs()
    }

    private fun printLoadAllMessages(){
        val messages = gameEngine.allMessage
        for (message in messages){
            if (message.isAgeText)
                addAgeTextViewToEvents(message.message)
            else
                addTextViewToEvents(message.message)
        }
        //fix for age messages
    }

    //used for when an activity is done and data is brought back
    private fun printAllMessages(){
        val messages = gameEngine.getAllMessages()
        for (message in messages){
            if (message.isAgeText)
                addAgeTextViewToEvents(message.message)
            else
                addTextViewToEvents(message.message)
        }
        for (message in messages){
            if (!message.isAgeText)
                Tools.showPopupDialog(this, message.message, "OK", "",null, null)
        }
        gameEngine.saveGameEngineToFile(this,"game_state.bin")
    }

    private fun printLogMessages(){
        val messages = gameEngine.getAllLogMessages()
        for (message in messages) {
            addTextViewToEvents(message.message)
        }
    }

    //Used for Age button
    private fun simulateUI() {
        if (gameEngine.startNew){
            startNewGame()
            gameEngine.startNew = false
        }
        changestatusUI()
        val event = gameEngine.randomEvents()
        if (event != null){
            Tools.showPopupDialog(this,
                event.first,
                event.second[0].first,
                event.second[1].first,
                null
            ) { resultCode, button ->}
        }
        printAllMessages()
        printLogMessages()
    }

    //Used by events from other pages
    private fun addTextViewToEvents(text: String){
        val textView = TextView(this)
        textView.text = text.replace("\n", "") // Set the text for the TextView
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
    private fun addAgeTextViewToEvents(text: String){
        val textView = TextView(this)
        val text = text
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

    private fun deleteAllEvents(){
        binding.eventLayout.removeAllViews()
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

        val moneyColor = if (player.money < 0) {
            ContextCompat.getColor(this, R.color.negativeCash)
        } else {
            ContextCompat.getColor(this, R.color.positiveCash)
        }
        binding.moneyText.setTextColor(moneyColor)
        binding.moneyText.text = Tools.formatMoney(player.money)
        binding.netWorthText.text = Tools.formatNetWorthMoney(player.netWorth)
        binding.playerName.text = player.name
        binding.workStatus.text = player.title
    }

    private fun resetSharedPrefs(){
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        val skills = mapOf(
            Skill.READ to "readSwitch",
            Skill.ACT to "actingSwitch",
            Skill.PRAY to "praySwitch",
            Skill.FIGHT to "fightSwitch",
            Skill.POLITICS to "politicsSwitch",
            Skill.CRIME to "crimeSwitch"
        )

        for ((skill, switchId) in skills) {
            editor.putBoolean("${skill.name}Switch", false)
        }

        // Apply the changes to the shared preferences
        editor.apply()
    }
}
