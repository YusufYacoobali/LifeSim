package com.example.lifesim4

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.os.Bundle
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.lifesim4.databinding.MainMainBinding
import com.example.lifesim4.models.GameEngine
import com.example.lifesim4.models.Person
import com.example.lifesim4.tools.Tools
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity()  {

    private lateinit var gameEngine: GameEngine
    private lateinit var binding: MainMainBinding
    private lateinit var player: Person
    companion object {
        private const val REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.main_main)
        setContentView(binding.root)

//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//            // Permission is granted, perform file write operations
//            //performFileWriteOperations()
//            startNewGame()
//        } else {
//            // Permission is not granted, request the permission
//            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_CODE)
//        }

        val gameEngineData = GameEngine.loadGameEngineFromFile(this, "game_state.bin")
        if (gameEngineData != null) {
            gameEngine = gameEngineData
            gameEngine.setPlayer(gameEngineData.getPlayer())
            player = gameEngineData.getPlayer()
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
                gameEngine.calcNetWorth()
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

//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
//        if (requestCode == REQUEST_CODE) {
//            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                // Permission is granted, perform file write operations
//               // performFileWriteOperations()
//            } else {
//                // Permission is denied, handle the scenario
//                Toast.makeText(this, "File write permission denied.", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }

    //Start new Life
    private fun startNewGame(){
        gameEngine.startGame()
        player = gameEngine.getPlayer()
        deleteAllEvents()
        binding.playerName.text = player.name
        binding.workStatus.text = "Baby"
        gameEngine.calcNetWorth()
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
        messages.reverse()
        for (message in messages){
            Tools.showPopupDialog(this, message, null, null)
        }
    }

    //Used for Age button
    private fun simulateUI() {
        changestatusUI()
        addAgeTextViewToEvents()
        printAllMessages()
        if (gameEngine.startNew == true){
            startNewGame()
            gameEngine.startNew = false

        }
        gameEngine.name = "test2"
        gameEngine.saveGameEngineToFile(this,"game_state.bin", gameEngine, player)
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
}
