package com.sim.lifesim4.activities

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import com.example.lifesim4.R
import com.sim.lifesim4.models.GameEngine
import com.sim.lifesim4.models.Person

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
                    if (player.gender){
                        //male player
                        GameEngine.generateLover(player.gender)
                        showFemalePopup()
                    } else {
                        GameEngine.generateLover(player.gender)
                        showMalePopup()
                    }
                }
            }
            setResult(Activity.RESULT_OK)
            finish()
        }

        val findLove: LinearLayout = findViewById(R.id.findLove)
        findLove.setOnClickListener(clickListener)
    }
}