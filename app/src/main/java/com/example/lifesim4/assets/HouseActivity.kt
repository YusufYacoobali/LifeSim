package com.example.lifesim4.assets

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.lifesim4.R
import com.example.lifesim4.models.Asset
import com.example.lifesim4.models.GameEngine
import com.example.lifesim4.models.HouseState
import com.example.lifesim4.models.Person

class HouseActivity : AppCompatActivity() {

    private lateinit var gameEngine: GameEngine
    private lateinit var player: Person
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.owned_house)
        gameEngine = GameEngine.getInstance()
        player = gameEngine.getPlayer()

        val currentContainer: LinearLayout = findViewById(R.id.current)
        val allHomesContainer: LinearLayout = findViewById(R.id.allHomes)

        val currentHome = player.assets.find { asset ->
            asset is Asset.House && asset.state == HouseState.LIVING_IN
        } as? Asset.House

        val allHomes = player.assets.filterIsInstance<Asset.House>().filter { it.state != HouseState.LIVING_IN }

        if (currentHome != null) {
            addHouseToView(currentContainer, currentHome.name , "${currentHome.squareFeet}sq ft  Condition ${currentHome.condition}%", R.drawable.home)
        }

        allHomes.forEach { home ->
            addHouseToView(allHomesContainer, home.name , "${home.squareFeet}sq ft  Condition ${home.condition}%", R.drawable.home)
        }
    }

    private fun addHouseToView(placement: LinearLayout, name: String?, caption: String, icon: Int){
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