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

class CharityActivity : AppCompatActivity() {
    private lateinit var gameEngine: GameEngine
    private lateinit var player: Person
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_charity)
        gameEngine = GameEngine.getInstance()
        player = gameEngine.getPlayer()

        val clickListener = View.OnClickListener { view ->
            when (view.id) {
                R.id.donateClothes -> {
                    val  (fortuneChange, charge) = player.charityOptions(1)
                    sendMessage(charge,"You donated some new clothes you had.\n Fortune: +${fortuneChange}\n")
                }
                R.id.giveMoney -> {
                    val  (fortuneChange, charge) = player.charityOptions(2)
                    sendMessage(charge, "You felt generous and gave 10% of what you had costing you\n ${Tools.formatMoney(charge)}" +
                            ".\n Fortune: +${fortuneChange}\n")
                }
                R.id.volunteer -> {
                    val  (fortuneChange, charge) = player.charityOptions(3)
                    sendMessage(charge, "You spent a day helping the orphans.\n Fortune: +${fortuneChange}\n")
                }
            }
            setResult(Activity.RESULT_OK)
            finish()
        }

        val donateClothes: LinearLayout = findViewById(R.id.donateClothes)
        val giveMoney: LinearLayout = findViewById(R.id.giveMoney)
        val volunteer: LinearLayout = findViewById(R.id.volunteer)
        donateClothes.setOnClickListener(clickListener)
        giveMoney.setOnClickListener(clickListener)
        volunteer.setOnClickListener(clickListener)
    }

    fun sendMessage(charge: Long, successMessage: String) {
        if (charge == -1L){
            gameEngine.sendMessage(
                GameEngine.Message(
                    "You have no money to donate",
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