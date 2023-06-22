package com.example.lifesim4.relations

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import com.example.lifesim4.R
import com.example.lifesim4.models.Character
import com.example.lifesim4.models.GameEngine
import com.example.lifesim4.models.Person

class PersonActivity : AppCompatActivity() {

    private lateinit var gameEngine: GameEngine
    private lateinit var player: Person

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.relation_person)
        gameEngine = GameEngine.getInstance()
        player = gameEngine.getPlayer()

        val nameTextView = findViewById<TextView>(R.id.name)
        val personName = intent.getStringExtra("ObjectName")
        if (personName != null) {
            val person: Character? = gameEngine.getPerson(personName)
            if (person == null) {
                gameEngine.sendMessage("No person found")
            } else {
                nameTextView.text = "${person.name}"
//                buttonTextView.setOnClickListener{
//                    //gameEngine.simulate()
//                    player.money -= 432
//                    player.children[0].money += 100
//                    //buttonTextView.text = "${person.name} ${player.children[0].money}"
//                    setResult(Activity.RESULT_OK)
//                    //finish()
//                }
            }
        } else {
            gameEngine.sendMessage("Invalid person name")
        }

    }
}