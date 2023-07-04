package com.example.lifesim4.assets

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.result.contract.ActivityResultContracts
import com.example.lifesim4.R
import com.example.lifesim4.models.Asset
import com.example.lifesim4.models.GameEngine
import com.example.lifesim4.models.Person
import com.example.lifesim4.tools.Tools

class PlanesActivity : AppCompatActivity() {
    private lateinit var gameEngine: GameEngine
    private lateinit var player: Person
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.owned_planes)
        gameEngine = GameEngine.getInstance()
        player = gameEngine.getPlayer()

        val myContract = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                setResult(Activity.RESULT_OK)
                //finish()
            }
        }

        val allPlanesContainer: LinearLayout = findViewById(R.id.allPlanes)

        val allPlanes = player.assets.filterIsInstance<Asset.Plane>()

        allPlanes.forEach { thing ->
            Tools.addCardToView(this, thing,  allPlanesContainer, "Condition ${thing.condition}%", R.drawable.buy_planes, AssetActivity::class.java, myContract)
        }
    }
}