package com.sim.lifesim4.activities

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import com.example.lifesim4.R
import com.sim.lifesim4.models.GameEngine
import com.sim.lifesim4.models.Person

class HolidayActivity : AppCompatActivity() {
    private lateinit var gameEngine: GameEngine
    private lateinit var player: Person

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_holiday)
        gameEngine = GameEngine.getInstance()
        player = gameEngine.getPlayer()

        val clickListener = View.OnClickListener { view ->
            when (view.id) {
                R.id.skiing -> {
                    val  (vitalityChange, charge) = player.holidayOptions(1)
                    sendMessage(charge, "15k", "You went on a skiing holiday and had a great time.\n Vitality: +${vitalityChange}\n")
                }
                R.id.resort -> {
                    val  (vitalityChange, charge) = player.holidayOptions(2)
                    sendMessage(charge, "8k", "You relaxed in a resort.\n Vitality: +${vitalityChange}\n")
                }
                R.id.cruise -> {
                    val  (vitalityChange, charge) = player.holidayOptions(3)
                    sendMessage(charge, "12k", "You went on a cruise and had a lovely time.\n Vitality: +${vitalityChange}\n")
                }
                R.id.affordable -> {
                    val  (vitalityChange, charge) = player.holidayOptions(4)
                    sendMessage(charge, "2k", "You took a cheap holiday.\n Vitality: +${vitalityChange}\n")
                }
            }
            setResult(Activity.RESULT_OK)
            finish()
        }

        val skiing: LinearLayout = findViewById(R.id.skiing)
        val resort: LinearLayout = findViewById(R.id.resort)
        val cruise: LinearLayout = findViewById(R.id.cruise)
        val affordable: LinearLayout = findViewById(R.id.affordable)
        skiing.setOnClickListener(clickListener)
        resort.setOnClickListener(clickListener)
        cruise.setOnClickListener(clickListener)
        affordable.setOnClickListener(clickListener)
    }

    fun sendMessage(charge: Long, minCost: String, successMessage: String) {
        if (charge == -1L){
            gameEngine.sendMessage(
                GameEngine.Message(
                    "Minimum charge is $${minCost}. You cant afford this holiday.",
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