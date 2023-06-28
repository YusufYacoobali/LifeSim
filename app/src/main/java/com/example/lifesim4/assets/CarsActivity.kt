package com.example.lifesim4.assets

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.result.contract.ActivityResultContracts
import com.example.lifesim4.R
import com.example.lifesim4.models.Asset
import com.example.lifesim4.models.AssetState
import com.example.lifesim4.models.GameEngine
import com.example.lifesim4.models.Person
import com.example.lifesim4.tools.Tools

class CarsActivity : AppCompatActivity() {
    private lateinit var gameEngine: GameEngine
    private lateinit var player: Person
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.owned_cars)
        gameEngine = GameEngine.getInstance()
        player = gameEngine.getPlayer()

        val myContract = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                setResult(Activity.RESULT_OK)
                //finish()
            }
        }

        val currentContainer: LinearLayout = findViewById(R.id.current)
        val allCarsContainer: LinearLayout = findViewById(R.id.allCars)

        val currentCar = player.assets.find { asset ->
            asset is Asset.Car && asset.state == AssetState.PRIMARY
        } as? Asset.Car

        val allCars = player.assets.filterIsInstance<Asset.Car>().filter { it.state != AssetState.PRIMARY}

        if (currentCar != null) {
            Tools.addCardToView(this, currentCar,  currentContainer, "Condition ${currentCar.condition}%", R.drawable.buy_car, AssetActivity::class.java, myContract)
        }

        allCars.forEach { thing ->
            Tools.addCardToView(this, thing,  allCarsContainer, "${thing.state}  Condition ${thing.condition}%", R.drawable.buy_car, AssetActivity::class.java, myContract)
        }
    }
}