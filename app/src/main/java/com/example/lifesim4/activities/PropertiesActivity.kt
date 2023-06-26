package com.example.lifesim4.activities

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import com.example.lifesim4.R
import com.example.lifesim4.models.Asset
import com.example.lifesim4.models.GameEngine
import com.example.lifesim4.models.HouseState
import com.example.lifesim4.models.Person
import com.example.lifesim4.tools.Tools
import com.example.lifesim4.tools.Tools.formatMoney

class PropertiesActivity : AppCompatActivity() {

    //ADD RENTING OPTION TOO ON POP UP DIALOG

    private lateinit var gameEngine: GameEngine
    private lateinit var asset: Asset
    private lateinit var player: Person
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_properties)
        gameEngine = GameEngine.getInstance()
        player = gameEngine.getPlayer()

        val myContract = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                setResult(Activity.RESULT_OK)
                //updateUI()
                finish()
            }
        }

        val houses = makeHouses().sortedByDescending { it.value }
        val houseContainer = findViewById<LinearLayout>(R.id.houseMarket)

        val cards = Tools.addCardsToView(this, houses,  houseContainer, "sq ft  Condition%", R.drawable.home, null, myContract)
        cards.forEach {card ->
            val house = card.obj as Asset.House
            val captionTextView: TextView = card.personCard.findViewById(R.id.caption)
            captionTextView.text = "${house.squareFeet}sq ft  Condition ${house.condition}%"
            card.personCard.setOnClickListener{
                Tools.showPopupDialog(this, "Would you like to buy for ${formatMoney(house.value.toLong())}", house) { resultCode ->
                    gameEngine.sendMessage("Bought a house")
                    setResult(resultCode)
                    finish()
                }
            }
        }
    }

    private fun makeHouses(): List<Asset.House> {
        val houseCounts = mapOf(
            PriceCategory.CHEAP to 4,
            PriceCategory.MEDIUM to 4,
            PriceCategory.HIGH to 4,
            PriceCategory.LUXURY to 3
        )

        val houses = mutableListOf<Asset.House>()

        houseCounts.forEach { (houseType, count) ->
            repeat(count) {
                houses += makeHouse(houseType)
            }
        }

        return houses
    }

    private fun makeHouse(houseType: PriceCategory): Asset.House{
        val name = getRandomHouseName(houseType)
        val price = getRandomPrice(houseType)
        val condition = getRandomCondition()
        val boughtFor = -1L
        val squareFoot = getRandomSqaureFoot(houseType)
        val state = HouseState.MARKET
        val house = Asset.House(name, price, condition, boughtFor, squareFoot, state)
        gameEngine.addAssets(house)
        return house
    }

    fun getRandomHouseName(houseType: PriceCategory): String {
        val houseNames = mapOf(
            PriceCategory.CHEAP to listOf(
                "Cozy Cottage", "Rustic Cabin", "Bungalow", "Small Retreat", "Quaint Haven",
                "Tiny Abode", "Simple Dwelling", "Charming Shack", "Affordable Home", "Budget Residence"
            ),
            PriceCategory.MEDIUM to listOf(
                "Modern Loft", "City Penthouse", "Contemporary Condo", "Urban Oasis", "Stylish Apartment",
                "Metropolitan Living", "Sleek Studio", "Elegant Loft", "Cosmopolitan Home", "Trendy Residence"
            ),
            PriceCategory.HIGH to listOf(
                "Luxury Villa", "Mansion", "Historic Estate", "Exquisite Manor", "Grand Chateau",
                "Opulent Mansion", "Prestigious Residence", "Elegant Abode", "Stately Dwelling", "Palatial Home"
            ),
            PriceCategory.LUXURY to listOf(
                "Castle", "Manor House", "Spanish Villa", "Royal Residence", "Regal Palace",
                "Imposing Fortress", "Grand Estate", "Majestic Mansion", "Magnificent Domicile", "Sumptuous Abode"
            )
        )

        return houseNames[houseType]?.random() ?: error("Invalid house type")
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