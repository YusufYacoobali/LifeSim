package com.sim.lifesim4.relations

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.result.contract.ActivityResultContracts
import com.example.lifesim4.R
import com.sim.lifesim4.models.AffectionType
import com.sim.lifesim4.models.GameEngine
import com.sim.lifesim4.models.Person
import com.sim.lifesim4.tools.Tools

class FriendsActivity : AppCompatActivity() {
    private lateinit var gameEngine: GameEngine
    private lateinit var player: Person
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.relation_friends)
        gameEngine = GameEngine.getInstance()
        player = gameEngine.getPlayer()

        val myContract = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                setResult(Activity.RESULT_OK)
                //finish()
            }
        }

        val bestFriendContainer: LinearLayout = findViewById(R.id.bestFriend)
        val friendsContainer: LinearLayout = findViewById(R.id.friendsAll)

        val bestFriend = player.friends.filter { person ->
            person.affectionType == AffectionType.BestFriend
        }

        val friends = player.friends.filter { person ->
            person.affectionType == AffectionType.Friend
        }

        bestFriend.forEach{ person ->
            Tools.addCardToView(this, person,  bestFriendContainer, "Health ${person.health}%", R.drawable.female, PersonActivity::class.java, myContract)
        }

        friends.forEach{ person ->
            Tools.addCardToView(this, person,  friendsContainer, "Health ${person.health}%", R.drawable.female, PersonActivity::class.java, myContract)
        }
    }
}