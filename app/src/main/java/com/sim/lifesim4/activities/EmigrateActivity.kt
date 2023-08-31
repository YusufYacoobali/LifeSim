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

class EmigrateActivity : AppCompatActivity() {
    private lateinit var gameEngine: GameEngine
    private lateinit var player: Person
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emigrate)
        gameEngine = GameEngine.getInstance()
        player = gameEngine.getPlayer()

        val clickListener = View.OnClickListener { view ->
            when (view.id) {
                R.id.emigrate -> {
                    val  (Change, charge) = player.emigrate()
                    sendMessage(charge,"You emigrated to another country for a better life costing you\n" +
                            " ${Tools.formatMoney(charge)}")
                }
            }
            setResult(Activity.RESULT_OK)
            finish()
        }

        val emigrate: LinearLayout = findViewById(R.id.emigrate)
        emigrate.setOnClickListener(clickListener)
    }

    fun sendMessage(charge: Long, successMessage: String) {
        if (charge == -1L){
            gameEngine.sendMessage(
                GameEngine.Message(
                    "You can't afford to move at this moment",
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