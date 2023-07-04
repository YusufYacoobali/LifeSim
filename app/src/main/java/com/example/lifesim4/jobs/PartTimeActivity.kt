package com.example.lifesim4.jobs

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.activity.result.contract.ActivityResultContracts
import com.example.lifesim4.R
import com.example.lifesim4.models.GameEngine
import com.example.lifesim4.models.GameEngine.*

class PartTimeActivity : AppCompatActivity() {

    private lateinit var gameEngine: GameEngine
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.job_part_time)
        gameEngine = GameEngine.getInstance()

        val myContract = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                //setResult(Activity.RESULT_OK)
                //finish()
            }
        }
        //val houses = makeJobs().sortedByDescending { it.value }
        //updatePage(houses, myContract)

        val partTime1: LinearLayout = findViewById(R.id.partTime1)
        val partTime2: LinearLayout = findViewById(R.id.partTime2)

        val clickListener = View.OnClickListener { view ->
            when (view.id) {
                R.id.partTime1 -> {
                    gameEngine.sendMessage(Message("You started working part time as a Life Guard with a salary of $13/hour", false))
                }
                R.id.partTime2 -> {
                    gameEngine.sendMessage(Message("You started working part time as a Window Cleaner with a salary of $15/hour", false))
                }
            }

            setResult(Activity.RESULT_OK)
            finish()
        }

        partTime1.setOnClickListener(clickListener)
        partTime2.setOnClickListener(clickListener)

    }
}