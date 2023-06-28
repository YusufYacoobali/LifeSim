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
import com.example.lifesim4.models.GameEngine
import com.example.lifesim4.models.HouseState
import com.example.lifesim4.models.Person
import com.example.lifesim4.tools.Tools
import com.example.lifesim4.tools.Tools.formatMoney
import kotlin.random.Random

class PropertiesActivity : AppCompatActivity() {

    private lateinit var gameEngine: GameEngine
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_properties)
        gameEngine = GameEngine.getInstance()

        val myContract = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                //setResult(Activity.RESULT_OK)
                //finish()
            }
        }
        val houses = makeHouses().sortedByDescending { it.value }
        updatePage(houses, myContract)
    }

    private fun updatePage(houses: List<Asset.House>, myContract: ActivityResultLauncher<Intent>) {
        val houseContainer = findViewById<LinearLayout>(R.id.houseMarket)
        houseContainer.removeAllViews()

        val marketHouses = houses.filter { it.state == HouseState.MARKET }
        val cards = Tools.addCardsToView(this, marketHouses, houseContainer, "sq ft  Condition%", R.drawable.home, null, myContract)

        cards.forEach { card ->
            val house = card.obj as Asset.House
            val captionTextView: TextView = card.personCard.findViewById(R.id.caption)
            captionTextView.text = "${house.squareFeet}sq ft  Condition ${house.condition}%"
            card.personCard.setOnClickListener {
                Tools.showPopupDialog(this, "Would you like to buy this for \n${formatMoney(house.value.toLong())}", house) { resultCode ->
                    gameEngine.sendMessage("You bought a ${house.name} for\n${formatMoney(house.value.toLong())}")
                    setResult(resultCode)
                    updatePage(houses, myContract)
                    //finish()
                }
            }
        }
    }

    private fun makeHouses(): List<Asset.House> {
        val houseCounts = mapOf(
            PriceCategory.CHEAP to Random.nextInt(2, 4),
            PriceCategory.MEDIUM to Random.nextInt(1, 3),
            PriceCategory.HIGH to Random.nextInt(1, 3),
            PriceCategory.LUXURY to Random.nextInt(0, 4)
        )

        val houses = mutableListOf<Asset.House>()

        houseCounts.forEach { (houseType, count) ->
            repeat(count) {
                houses += makeHouse(houseType)
            }
        }
        return houses
    }

    private fun makeHouse(houseType: PriceCategory): Asset.House {
        val (houseName, houseIcon) = getRandomHouseName(houseType)
        val price = getRandomPrice(houseType)
        val condition = getRandomCondition()
        val boughtFor = -1L
        val squareFoot = getRandomSqaureFoot(houseType)
        val state = HouseState.MARKET
        val house = Asset.House(Asset.getNextId(), houseName, price, condition, boughtFor, squareFoot, state, houseIcon)
        gameEngine.addAssets(house)
        return house
    }

    fun getRandomHouseName(houseType: PriceCategory): Pair<String, Int> {
        val houseNames = mapOf(
            PriceCategory.CHEAP to listOf(
                Pair("Cozy Cottage", R.drawable.home_cheap_1),
                Pair("Rustic Cabin", R.drawable.home),
                Pair("Bungalow", R.drawable.home_cheap_1),
                // Add more cheap house names and their icons
            ),
            PriceCategory.MEDIUM to listOf(
                Pair("Modern Loft", R.drawable.home_medium_1),
                Pair("City Penthouse", R.drawable.home_medium_1),
                Pair("Contemporary Condo", R.drawable.home_medium_1),
                // Add more medium house names and their icons
            ),
            PriceCategory.HIGH to listOf(
                Pair("Luxury Villa", R.drawable.home_high_1),
                Pair("Mansion", R.drawable.home_high_1),
                Pair("Historic Estate", R.drawable.home_high_1),
                // Add more high house names and their icons
            ),
            PriceCategory.LUXURY to listOf(
                Pair("Castle", R.drawable.home_luxury_1),
                Pair("Manor House", R.drawable.home_luxury_1),
                Pair("Spanish Villa", R.drawable.home_luxury_1),
                // Add more luxury house names and their icons
            )
        )

        val houseList = houseNames[houseType] ?: error("Invalid house type")
        return houseList.random()
    }


    fun getRandomPrice(houseType: PriceCategory): Double {
        return when (houseType) {
            PriceCategory.CHEAP -> (44000..200000).random().toDouble()
            PriceCategory.MEDIUM -> (200000..800000).random().toDouble()
            PriceCategory.HIGH -> (1000000..5000000).random().toDouble()
            PriceCategory.LUXURY -> (1000000000..5000000000).random().toDouble()
        }
    }

    fun getRandomCondition(): Int {
        val minCondition = 0
        val maxCondition = 100
        return (minCondition..maxCondition).random()
    }

    fun getRandomSqaureFoot(houseType: PriceCategory): Int {
        val cheapSquareFootRange = 200..999
        val mediumSquareFootRange = 1000..1999
        val highSquareFootRange = 2000..2999
        val luxurySquareFootRange = 3000..9999

        return when (houseType) {
            PriceCategory.CHEAP -> cheapSquareFootRange.random()
            PriceCategory.MEDIUM -> mediumSquareFootRange.random()
            PriceCategory.HIGH -> highSquareFootRange.random()
            PriceCategory.LUXURY -> luxurySquareFootRange.random()
        }
    }
    enum class PriceCategory {
        CHEAP,
        MEDIUM,
        HIGH,
        LUXURY
    }

}