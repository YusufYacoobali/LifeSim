package com.example.lifesim4.relations

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.result.contract.ActivityResultContracts
import com.example.lifesim4.R
import com.example.lifesim4.models.AffectionType
import com.example.lifesim4.models.GameEngine
import com.example.lifesim4.models.Person
import com.example.lifesim4.tools.Tools

class LoversActivity : AppCompatActivity() {
    private lateinit var gameEngine: GameEngine
    private lateinit var player: Person
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.relation_lovers)
        gameEngine = GameEngine.getInstance()
        player = gameEngine.getPlayer()

        val myContract = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                setResult(Activity.RESULT_OK)
                //finish()
            }
        }

        val wifeContainer: LinearLayout = findViewById(R.id.wife)
        val girlfriendsContainer: LinearLayout = findViewById(R.id.girlfriends)

        val wives = player.lovers.filter { lover ->
            lover.affectionType == AffectionType.Wife
        }

        val gfs = player.lovers.filter { lover ->
            lover.affectionType == AffectionType.Girlfriend
        }

        wives.forEach{ person ->
            Tools.addCardToView(this, person,  wifeContainer, "Health ${person.health}%", R.drawable.male, PersonActivity::class.java, myContract)
        }

        gfs.forEach{ person ->
            Tools.addCardToView(this, person,  girlfriendsContainer, "Health ${person.health}%", R.drawable.male, PersonActivity::class.java, myContract)
        }
    }
}