package com.example.lifesim4.relations

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import com.example.lifesim4.R
import com.example.lifesim4.models.Asset
import com.example.lifesim4.models.Character
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

        val brothers = Tools.addCardsToView(this, player.brothers,  siblingsContainer, "test", R.drawable.male, PersonActivity::class.java, myContract)
        val sisters = Tools.addCardsToView(this, player.sisters,  siblingsContainer, "test", R.drawable.male, PersonActivity::class.java, myContract)
        val children = Tools.addCardsToView(this, player.children,  childrenContainer, "test", R.drawable.baby, PersonActivity::class.java, myContract)

        val allFamilyMembers = brothers + sisters + children

        allFamilyMembers.forEach { card ->
            val person = card.obj as Character
            val captionTextView: TextView = card.personCard.findViewById(R.id.caption)
            captionTextView.text = "Health ${person.health}%"
        }
    }
}