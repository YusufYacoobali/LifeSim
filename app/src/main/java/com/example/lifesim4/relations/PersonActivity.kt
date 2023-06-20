package com.example.lifesim4.relations

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import com.example.lifesim4.R
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

        val buttonTextView = findViewById<TextView>(R.id.button)

        buttonTextView.text = player.children[0].name
        buttonTextView.setOnClickListener{
            //gameEngine.simulate()
            player.money -= 432
            setResult(Activity.RESULT_OK)
            //finish()
        }

    }
}