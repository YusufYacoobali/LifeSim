package com.example.lifesim4.relations

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import com.example.lifesim4.R
import com.example.lifesim4.models.AffectionType
import com.example.lifesim4.models.GameEngine
import com.example.lifesim4.models.Person
import com.example.lifesim4.tools.Tools

class EnemiesActivity : AppCompatActivity() {
    private lateinit var gameEngine: GameEngine
    private lateinit var player: Person
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.relation_enemies)
        gameEngine = GameEngine.getInstance()
        player = gameEngine.getPlayer()

        val enemiesContainer: LinearLayout = findViewById(R.id.enemies)

        val myContract = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                setResult(Activity.RESULT_OK)
                //finish()
            }
        }

        val enemies = player.enemies.filter { person ->
            person.affectionType == AffectionType.Enemy
        }

        enemies.forEach{ person ->
            Tools.addCardToView(this, person,  enemiesContainer, "Health ${person.health}%", R.drawable.female, PersonActivity::class.java, myContract)
        }
    }
}