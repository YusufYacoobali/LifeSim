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
import com.example.lifesim4.tools.Tools.formatMoney
import kotlin.random.Random

class PlanesActivity : AppCompatActivity() {

    private lateinit var gameEngine: GameEngine
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_planes)
        gameEngine = GameEngine.getInstance()

        val myContract = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                //setResult(Activity.RESULT_OK)
                //finish()
            }
        }
        val planes = makeAircraft().sortedByDescending { it.value }
        updatePage(planes, myContract)
    }

    private fun updatePage(planes: List<Asset.Plane>, myContract: ActivityResultLauncher<Intent>) {
        val planeContainer = findViewById<LinearLayout>(R.id.planeMarket)
        planeContainer.removeAllViews()

        val marketBoats = planes.filter { it.state == AssetState.MARKET }
        val cards = Tools.addCardsToView(this, marketBoats, planeContainer, "car", R.drawable.buy_planes, null, myContract)

        cards.forEach { card ->
            val plane = card.obj as Asset.Plane
            val captionTextView: TextView = card.personCard.findViewById(R.id.caption)
            captionTextView.text = "Condition ${plane.condition}%"
            card.personCard.setOnClickListener {
                Tools.showPopupDialog(this, "Would you like to buy this for \n${
                    Tools.formatMoney(
                        plane.value.toLong()
                    )
                }", "Use", "Buy", plane) { resultCode, button ->
                    if (button == 1){
                        //rent property
                    } else if (button == 2){
                        gameEngine.sendMessage(
                            GameEngine.Message(
                                "You bought a ${plane.name} for\n${
                                    formatMoney(
                                        plane.value.toLong()
                                    )
                                }", false
                            )
                        )
                        gameEngine.buyAsset(plane)
                        gameEngine.addAssets(plane)
                        setResult(resultCode)
                    }
                    updatePage(planes, myContract)
                    //finish()
                }
            }
        }
    }

    private fun makeAircraft(): List<Asset.Plane> {
        val aircraftCounts = mapOf(
            PriceCategory.CHEAP to Random.nextInt(2, 4),
            PriceCategory.MEDIUM to Random.nextInt(1, 3),
            PriceCategory.HIGH to Random.nextInt(1, 3),
            PriceCategory.LUXURY to Random.nextInt(0, 4)
        )

        val aircraftList = mutableListOf<Asset.Plane>()

        aircraftCounts.forEach { (aircraftType, count) ->
            repeat(count) {
                aircraftList += makeAircraft(aircraftType)
            }
        }
        return aircraftList
    }

    private fun makeAircraft(aircraftPriceType: PriceCategory): Asset.Plane {
        val (aircraftName, aircraftIcon) = getRandomAircraftName(aircraftPriceType)
        val price = getRandomPrice(aircraftPriceType)
        val condition = getRandomCondition()
        val boughtFor = -1L
        val aircraft = Asset.Plane(Asset.getNextId(), aircraftName, price, condition, boughtFor, aircraftIcon, AssetState.MARKET)
        return aircraft
    }

    fun getRandomAircraftName(aircraftType: PriceCategory): Pair<String, Int> {
        val aircraftNames = mapOf(
            PriceCategory.CHEAP to listOf(
                Pair("Small Airplane", R.drawable.plane_cheap_1),
                Pair("Ultralight Aircraft", R.drawable.plane_cheap_1),
                Pair("Glider", R.drawable.plane_cheap_1),
                // Add more cheap aircraft names and icons
            ),
            PriceCategory.MEDIUM to listOf(
                Pair("Business Jet", R.drawable.plane_medium_1),
                Pair("Propeller Aircraft", R.drawable.plane_medium_1),
                Pair("Amphibious Aircraft", R.drawable.plane_medium_1),
                // Add more medium aircraft names and icons
            ),
            PriceCategory.HIGH to listOf(
                Pair("Private Jet", R.drawable.plane_high_1),
                Pair("Turbojet Aircraft", R.drawable.plane_high_1),
                Pair("Air Ambulance", R.drawable.plane_high_1),
                // Add more high aircraft names and icons
            ),
            PriceCategory.LUXURY to listOf(
                Pair("Supersonic Jet", R.drawable.plane_luxury_1),
                Pair("Airbus A380", R.drawable.plane_luxury_1),
                Pair("Boeing 747", R.drawable.plane_luxury_1),
                // Add more luxury aircraft names and icons
            )
        )
        val aircraftList = aircraftNames[aircraftType] ?: error("Invalid aircraft type")
        return aircraftList.random()
    }

    fun getRandomPrice(aircraftType: PriceCategory): Double {
        return when (aircraftType) {
            PriceCategory.CHEAP -> (1000000..3000000).random().toDouble()
            PriceCategory.MEDIUM -> (3000000..8000000).random().toDouble()
            PriceCategory.HIGH -> (8000000..15000000).random().toDouble()
            PriceCategory.LUXURY -> (15000000..500000000).random().toDouble()
        }
    }

    fun getRandomCondition(): Int {
        val minCondition = 0
        val maxCondition = 100
        return (minCondition..maxCondition).random()
    }
}