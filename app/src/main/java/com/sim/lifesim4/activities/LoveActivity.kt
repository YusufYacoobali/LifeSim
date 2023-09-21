package com.sim.lifesim4.activities

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.LinearLayout
import android.widget.TextView
import com.example.lifesim4.R
import com.sim.lifesim4.models.GameEngine
import com.sim.lifesim4.models.NPC
import com.sim.lifesim4.models.Person
import com.sim.lifesim4.tools.Tools

class LoveActivity : AppCompatActivity() {
    private lateinit var gameEngine: GameEngine
    private lateinit var player: Person
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_love)
        gameEngine = GameEngine.getInstance()
        player = gameEngine.getPlayer()

        val clickListener = View.OnClickListener { view ->
            when (view.id) {
                R.id.findLove -> {
                    //check gender, you found a lovely, in a new pop up, their looks, personality, networth, crazy, fame, title
                    if (player.age >= 18){
                        val stranger = gameEngine.generateLover(player.gender)
                        showPopup(stranger)
                    } else {
                        Tools.showPopupDialog(
                            this,
                            "You need to be 18+",
                            "OK",
                            "",
                            null
                        ) { resultCode, button ->}
                    }
                }
            }
            setResult(Activity.RESULT_OK)
           // finish()
        }
        val findLove: LinearLayout = findViewById(R.id.findLove)
        findLove.setOnClickListener(clickListener)
    }

    fun showPopup(person: NPC){
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_love_popup)

        val dialogMessage: TextView = dialog.findViewById(R.id.dialog_message)
        val dialogButton: TextView = dialog.findViewById(R.id.dialog_button)
        val dialogButton2: TextView = dialog.findViewById(R.id.dialog_button2)

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialogMessage.text = "Name: ${person.name} \nAge: ${person.age}\nLooks: ${person.charm}\nPersonality: ${(person.health*person.charm)/100}\nNet Worth: $${person.money}"

        //if (obj is Asset){
        dialogButton.setOnClickListener {
            setResult(Activity.RESULT_OK)
            gameEngine.sendMessage(GameEngine.Message("You are now dating ${person.name}", false))
            player.date(person)
            finish()
        }
        dialogButton2.setOnClickListener {
            setResult(Activity.RESULT_OK)
            finish()
        }

        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        dialog.show()
    }
}