package com.example.lifesim4.relations

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import com.example.lifesim4.R
import com.example.lifesim4.RelationsActivity
import com.example.lifesim4.models.GameEngine
import com.example.lifesim4.models.Person
import com.example.lifesim4.tools.Tools

class FamilyActivity : AppCompatActivity() {

    private lateinit var gameEngine: GameEngine
    private lateinit var player: Person
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.relation_family)
        gameEngine = GameEngine.getInstance()
        player = gameEngine.getPlayer()

        val myContract = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                setResult(Activity.RESULT_OK)
                //finish()
            }
        }

        val parentsContainer: LinearLayout = findViewById(R.id.parents)
        val siblingsContainer: LinearLayout = findViewById(R.id.siblings)
        val childrenContainer: LinearLayout = findViewById(R.id.children)

        Tools.addCardToView(this, player.father,  parentsContainer, "Health ${player.father?.health}%", R.drawable.male, PersonActivity::class.java, myContract)
        Tools.addCardToView(this, player.mother,  parentsContainer, "Health ${player.mother?.health}%", R.drawable.female, PersonActivity::class.java, myContract)

        player.brothers.forEach{ person ->
            Tools.addCardToView(this, person,  siblingsContainer, "Health ${person.health}%", R.drawable.male, PersonActivity::class.java, myContract)
        }
        player.sisters.forEach{ person ->
            Tools.addCardToView(this, person, siblingsContainer, "Health ${person.health}%", R.drawable.female, PersonActivity::class.java, myContract)
        }
        player.children.forEach{ person ->
            Tools.addCardToView(this, person, childrenContainer, "Health ${person.health}%", R.drawable.baby, PersonActivity::class.java, myContract)
        }
    }
}