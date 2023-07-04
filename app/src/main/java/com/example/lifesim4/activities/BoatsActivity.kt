package com.example.lifesim4.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.lifesim4.R
import com.example.lifesim4.models.Asset
import com.example.lifesim4.models.AssetState
import com.example.lifesim4.models.GameEngine
import com.example.lifesim4.models.PriceCategory
import com.example.lifesim4.tools.Tools
import kotlin.random.Random

class BoatsActivity : AppCompatActivity() {

    private lateinit var gameEngine: GameEngine

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_boats)
        gameEngine = GameEngine.getInstance()

        val myContract = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                //setResult(Activity.RESULT_OK)
                //finish()
            }
        }
        val boats = makeBoats().sortedByDescending { it.value }
        updatePage(boats, myContract)
    }

    private fun updatePage(cars: List<Asset.Boat>, myContract: ActivityResultLauncher<Intent>) {
        val boatContainer = findViewById<LinearLayout>(R.id.boatMarket)
        boatContainer.removeAllViews()

        val marketBoats = cars.filter { it.state == AssetState.MARKET }
        val cards = Tools.addCardsToView(this, marketBoats, boatContainer, "car", R.drawable.buy_car, null, myContract)

        cards.forEach { card ->
            val boat = card.obj as Asset.Boat
            val captionTextView: TextView = card.personCard.findViewById(R.id.caption)
            captionTextView.text = "Condition ${boat.condition}%"
            card.personCard.setOnClickListener {
                Tools.showPopupDialog(this, "Would you like to buy this for \n${
                    Tools.formatMoney(
                        boat.value.toLong()
                    )
                }", boat) { resultCode ->
                    gameEngine.sendMessage(
                        GameEngine.Message(
                            "You bought a ${boat.name} for\n${
                                Tools.formatMoney(
                                    boat.value.toLong()
                                )
                            }", false
                        )
                    )
                    setResult(resultCode)
                    updatePage(cars, myContract)
                    //finish()
                }
            }
        }
    }

    private fun makeBoats(): List<Asset.Boat> {
        val boatCounts = mapOf(
            PriceCategory.CHEAP to Random.nextInt(2, 4),
            PriceCategory.MEDIUM to Random.nextInt(1, 3),
            PriceCategory.HIGH to Random.nextInt(1, 3),
            PriceCategory.LUXURY to Random.nextInt(0, 4)
        )

        val boats = mutableListOf<Asset.Boat>()

        boatCounts.forEach { (boatType, count) ->
            repeat(count) {
                boats += makeBoat(boatType)
            }
        }
        return boats
    }

    private fun makeBoat(boatPriceType: PriceCategory): Asset.Boat {
        val (boatName, boatIcon) = getRandomBoatName(boatPriceType)
        val price = getRandomPrice(boatPriceType)
        val condition = getRandomCondition()
        val boughtFor = -1L
        val boat = Asset.Boat(Asset.getNextId(), boatName, price, condition, boughtFor, boatIcon, AssetState.MARKET)
        gameEngine.addAssets(boat)
        return boat
    }

    fun getRandomBoatName(boatType: PriceCategory): Pair<String, Int> {
        val boatNames = mapOf(
            PriceCategory.CHEAP to listOf(
                Pair("Speedboat", R.drawable.boat_cheap_1),
                Pair("Inflatable Boat", R.drawable.boat_cheap_1),
                Pair("Canoe", R.drawable.boat_cheap_1),
                // Add more cheap boat names and icons
            ),
            PriceCategory.MEDIUM to listOf(
                Pair("Sailboat", R.drawable.boat_medium_1),
                Pair("Fishing Boat", R.drawable.boat_medium_1),
                Pair("Kayak", R.drawable.boat_medium_1),
                // Add more medium boat names and icons
            ),
            PriceCategory.HIGH to listOf(
                Pair("Yacht", R.drawable.boat_high_1),
                Pair("Cruiser", R.drawable.boat_high_1),
                Pair("Jet Ski", R.drawable.boat_high_1),
                // Add more high boat names and icons
            ),
            PriceCategory.LUXURY to listOf(
                Pair("Luxury Yacht", R.drawable.boat_luxury_1),
                Pair("Catamaran", R.drawable.boat_luxury_1),
                Pair("Houseboat", R.drawable.boat_luxury_1),
                // Add more luxury boat names and icons
            )
        )
        val boatList = boatNames[boatType] ?: error("Invalid boat type")
        return boatList.random()
    }

    fun getRandomPrice(carType: PriceCategory): Double {
        return when (carType) {
            PriceCategory.CHEAP -> (100000..300000).random().toDouble()
            PriceCategory.MEDIUM -> (300000..800000).random().toDouble()
            PriceCategory.HIGH -> (800000..1500000).random().toDouble()
            PriceCategory.LUXURY -> (1500000..50000000).random().toDouble()
        }
    }

    fun getRandomCondition(): Int {
        val minCondition = 0
        val maxCondition = 100
        return (minCondition..maxCondition).random()
    }

}