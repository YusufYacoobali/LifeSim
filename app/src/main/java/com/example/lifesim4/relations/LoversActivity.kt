package com.example.lifesim4.relations

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.lifesim4.R
import com.example.lifesim4.models.AffectionType
import com.example.lifesim4.models.GameEngine
import com.example.lifesim4.models.NPC
import com.example.lifesim4.models.Person

class LoversActivity : AppCompatActivity() {
    private lateinit var gameEngine: GameEngine
    private lateinit var player: Person
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.relation_lovers)
        gameEngine = GameEngine.getInstance()
        player = gameEngine.getPlayer()

        val wifeContainer: LinearLayout = findViewById(R.id.wife)
        val girlfriendsContainer: LinearLayout = findViewById(R.id.girlfriends)

        val wives = player.lovers.filter { lover ->
            lover.affectionType == AffectionType.Wife
        }

        val gfs = player.lovers.filter { lover ->
            lover.affectionType == AffectionType.Girlfriend
        }

        wives.forEach{ person ->
            addPersonToView(wifeContainer, person.name , "Health ${person.health}%", R.drawable.female)
        }

        gfs.forEach{ person ->
            addPersonToView(girlfriendsContainer, person.name , "Health ${person.health}%", R.drawable.female)
        }
    }

    private fun addPersonToView(placement: LinearLayout, name: String?, caption: String, icon: Int){
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