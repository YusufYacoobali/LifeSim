package com.example.lifesim4.relations

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.lifesim4.R
import com.example.lifesim4.models.AffectionType
import com.example.lifesim4.models.GameEngine
import com.example.lifesim4.models.Person

class EnemiesActivity : AppCompatActivity() {
    private lateinit var gameEngine: GameEngine
    private lateinit var player: Person
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.relation_enemies)
        gameEngine = GameEngine.getInstance()
        player = gameEngine.getPlayer()

        val enemiesContainer: LinearLayout = findViewById(R.id.enemies)

        val enemies = player.enemies.filter { person ->
            person.affectionType == AffectionType.Enemy
        }

        enemies.forEach{ person ->
            addPersonToView(enemiesContainer, person.name , "Health ${person.health}%", R.drawable.female)
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