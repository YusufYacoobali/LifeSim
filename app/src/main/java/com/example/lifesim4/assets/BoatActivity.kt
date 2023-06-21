package com.example.lifesim4.assets

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.lifesim4.R
import com.example.lifesim4.models.Asset
import com.example.lifesim4.models.GameEngine
import com.example.lifesim4.models.Person

class BoatActivity : AppCompatActivity() {
    private lateinit var gameEngine: GameEngine
    private lateinit var player: Person
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.owned_boat)
        gameEngine = GameEngine.getInstance()
        player = gameEngine.getPlayer()

        val allBoatsContainer: LinearLayout = findViewById(R.id.allBoats)
        val allBoats = player.assets.filterIsInstance<Asset.Boat>()

        allBoats.forEach { home ->
            addBoatToView(allBoatsContainer, home.name , "Condition ${home.condition}%", R.drawable.buy_boat)
        }
    }

    private fun addBoatToView(placement: LinearLayout, name: String?, caption: String, icon: Int){
        // Create an instance of the card_basic layout
        val personCard = layoutInflater.inflate(R.layout.card_basic, placement, false)

        // Find the views inside the fatherCard layout and set the father's details
        val nameTextView: TextView = personCard.findViewById(R.id.name)
        val captionTextView: TextView = personCard.findViewById(R.id.caption)
        val image: ImageView = personCard.findViewById(R.id.image)

        nameTextView.text = name
        captionTextView.text = caption
        image.setImageResource(icon)
        placement.addView(personCard)
        //return personCard
    }
}