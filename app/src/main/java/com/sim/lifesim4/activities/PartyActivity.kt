package com.sim.lifesim4.activities

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import com.example.lifesim4.R
import com.sim.lifesim4.models.GameEngine
import com.sim.lifesim4.models.Person
import com.sim.lifesim4.tools.Tools

class PartyActivity : AppCompatActivity() {
    private lateinit var gameEngine: GameEngine
    private lateinit var player: Person
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_party)
        gameEngine = GameEngine.getInstance()
        player = gameEngine.getPlayer()

        //invisible for now, for future update
        val casino: LinearLayout = findViewById(R.id.casino)
        casino.visibility = View.GONE

        val clickListener = View.OnClickListener { view ->
            when (view.id) {
                R.id.dayOut -> {
                    val  (vitalityChange, charge) = player.partyOptions(1)
                    sendMessage(charge, "40", "You had a great day out.\n Vitality: +${vitalityChange}\n")
                }
                R.id.worldRecord -> {
                    val  (vitalityChange, charge) = player.partyOptions(2)
                    sendMessage(charge, "0", "You broke the world record for most rain water collected in a minute" +
                            ".\n Vitality: +${vitalityChange}\n")
                }
                R.id.spa -> {
                    val  (vitalityChange, charge) = player.partyOptions(3)
                    sendMessage(charge, "150", "You had a lovely spa day.\n Vitality: +${vitalityChange}\n")
                }
                R.id.hostParty -> {
                    val  (vitalityChange, charge) = player.partyOptions(4)
                    sendMessage(charge, "200", "You hosted a party. The guests loved it.\n Vitality: +${vitalityChange}\n")
                }
            }
            setResult(Activity.RESULT_OK)
            finish()
        }

        val dayOut: LinearLayout = findViewById(R.id.dayOut)
        val worldRecord: LinearLayout = findViewById(R.id.worldRecord)
        val spa: LinearLayout = findViewById(R.id.spa)
        val hostParty: LinearLayout = findViewById(R.id.hostParty)
        //sort out money options etc for casino later on
        //val casino: LinearLayout = findViewById(R.id.casino)
        dayOut.setOnClickListener(clickListener)
        worldRecord.setOnClickListener(clickListener)
        spa.setOnClickListener(clickListener)
        hostParty.setOnClickListener(clickListener)
        //casino.setOnClickListener(clickListener)
    }

    fun sendMessage(charge: Long, minCost: String, successMessage: String) {
        if (charge == -1L){
            gameEngine.sendMessage(
                GameEngine.Message(
                    "Minimum charge is $${minCost}. You are broke and cannot afford this.",
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