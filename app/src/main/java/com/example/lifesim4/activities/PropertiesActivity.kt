package com.example.lifesim4.activities

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.result.contract.ActivityResultContracts
import com.example.lifesim4.R
import com.example.lifesim4.models.Asset
import com.example.lifesim4.models.GameEngine
import com.example.lifesim4.models.HouseState
import com.example.lifesim4.models.Person
import com.example.lifesim4.tools.Tools

class PropertiesActivity : AppCompatActivity() {

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

        val houses = makeHouses()

        val houseContainer = findViewById<LinearLayout>(R.id.houseMarket)
        //houseContainer.removeAllViews()

        houses.forEach { thing ->
            Tools.addCardToView(this, thing,  houseContainer, "${thing.squareFeet}sq ft  Condition ${thing.condition}%", R.drawable.home, null, myContract)
            setResult(Activity.RESULT_OK)
            //dont need to send in contract, coz gotta buy the house instead
        }

    }

    private fun makeHouses(): List<Asset.House> {
        val houses = mutableListOf<Asset.House>()
        repeat(2) {
            val house = makeHouse()
            houses.add(house)
        }
        return houses
    }

    private fun makeHouse(): Asset.House{
        val name = getRandomHouseName()
        val price = getRandomPrice()
        val condition = getRandomCondition()
        val boughtFor = -1L
        val squareFoot = getRandomSqaureFoot()
        val state = HouseState.MARKET
        val house = Asset.House(name, price, condition, boughtFor, squareFoot, state)
        gameEngine.addAssets(house)
        return house
    }

    fun getRandomHouseName(): String {
        val names = listOf("Cozy Cottage", "Victorian Manor", "Modern Loft", "Rustic Cabin")
        return names.random()
    }

    fun getRandomPrice(): Double {
        val minPrice = 43000
        val maxPrice = 99999999
        return (minPrice..maxPrice).random().toDouble()
    }

    fun getRandomCondition(): Int {
        val minCondition = 0
        val maxCondition = 100
        return (minCondition..maxCondition).random()
    }

    fun getRandomSqaureFoot(): Int {
        val minRent = 200
        val maxRent = 9999
        return (minRent..maxRent).random()
    }
}