package com.example.lifesim4.assets

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import com.example.lifesim4.R
import com.example.lifesim4.models.Asset
import com.example.lifesim4.models.GameEngine
import com.example.lifesim4.models.HouseState
import com.example.lifesim4.models.Person
import com.example.lifesim4.tools.Tools

class HouseActivity : AppCompatActivity() {

    private lateinit var gameEngine: GameEngine
    private lateinit var player: Person
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.owned_house)
        gameEngine = GameEngine.getInstance()
        player = gameEngine.getPlayer()

        val myContract = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                setResult(Activity.RESULT_OK)
                //updateUI()
                finish()
            }
        }

        val currentContainer: LinearLayout = findViewById(R.id.current)
        val allHomesContainer: LinearLayout = findViewById(R.id.allHomes)

        val currentHome = player.assets.find { asset ->
            asset is Asset.House && asset.state == HouseState.LIVING_IN
        } as? Asset.House

        val allHomes = player.assets.filterIsInstance<Asset.House>().filter { it.state != HouseState.LIVING_IN }

        if (currentHome != null) {
            Tools.addCardToView(this, currentHome,  currentContainer, "${currentHome.squareFeet}sq ft  Condition ${currentHome.condition}%", R.drawable.home, AssetActivity::class.java, myContract)
        }

        allHomes.forEach { thing ->
            Tools.addCardToView(this, thing,  allHomesContainer, "${thing.squareFeet}sq ft  Condition ${thing.condition}%", R.drawable.home, AssetActivity::class.java, myContract)
        }
    }

//    fun updateUI(){
//        val currentContainer: LinearLayout = findViewById(R.id.current)
//        val allHomesContainer: LinearLayout = findViewById(R.id.allHomes)
//
//        val currentHome = player.assets.find { asset ->
//            asset is Asset.House && asset.state == HouseState.LIVING_IN
//        } as? Asset.House
//
//        val allHomes = player.assets.filterIsInstance<Asset.House>().filter { it.state != HouseState.LIVING_IN }
//
//        if (currentHome != null) {
//            Tools.addCardToView(this, currentHome,  currentContainer, "${currentHome.squareFeet}sq ft  Condition ${currentHome.condition}%", R.drawable.home, AssetActivity::class.java, myContract)
//        }
//
//        allHomes.forEach { thing ->
//            Tools.addCardToView(this, thing,  allHomesContainer, "${thing.squareFeet}sq ft  Condition ${thing.condition}%", R.drawable.home, AssetActivity::class.java, myContract)
//        }
//    }
}