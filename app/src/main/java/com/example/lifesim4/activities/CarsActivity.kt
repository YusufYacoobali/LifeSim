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
import com.example.lifesim4.models.CarType
import com.example.lifesim4.models.PriceCategory
import com.example.lifesim4.tools.Tools
import kotlin.random.Random

class CarsActivity : AppCompatActivity() {

    private lateinit var gameEngine: GameEngine
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cars)
        gameEngine = GameEngine.getInstance()

        val myContract = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                //setResult(Activity.RESULT_OK)
                //finish()
            }
        }
        val cars = makeCars().sortedByDescending { it.value }
        updatePage(cars, myContract)
    }

    private fun updatePage(cars: List<Asset.Car>, myContract: ActivityResultLauncher<Intent>) {
        val carContainer = findViewById<LinearLayout>(R.id.carMarket)
        carContainer.removeAllViews()

        val marketHouses = cars.filter { it.state == AssetState.MARKET }
        val cards = Tools.addCardsToView(this, marketHouses, carContainer, "car", R.drawable.buy_car, null, myContract)

        cards.forEach { card ->
            val car = card.obj as Asset.Car
            val captionTextView: TextView = card.personCard.findViewById(R.id.caption)
            captionTextView.text = "Condition ${car.condition}%"
            card.personCard.setOnClickListener {
                Tools.showPopupDialog(this, "Would you like to buy this for \n${
                    Tools.formatMoney(
                        car.value.toLong()
                    )
                }", "Lease", "Buy", car) { resultCode, button ->
                    gameEngine.sendMessage(
                        GameEngine.Message(
                            "You bought a ${car.name} for\n${
                                Tools.formatMoney(
                                    car.value.toLong()
                                )
                            }", false
                        )
                    )
                    gameEngine.addAssets(car)
                    setResult(resultCode)
                    updatePage(cars, myContract)
                    //finish()
                }
            }
        }
    }

    private fun makeCars(): List<Asset.Car> {
        val carCounts = mapOf(
            PriceCategory.CHEAP to Random.nextInt(2, 4),
            PriceCategory.MEDIUM to Random.nextInt(1, 3),
            PriceCategory.HIGH to Random.nextInt(1, 3),
            PriceCategory.LUXURY to Random.nextInt(0, 4)
        )

        val cars = mutableListOf<Asset.Car>()

        carCounts.forEach { (carType, count) ->
            repeat(count) {
                cars += makeCar(carType)
            }
        }
        return cars
    }

    private fun makeCar(carPriceType: PriceCategory): Asset.Car {
        val (carName, carIcon) = getRandomCarName(carPriceType)
        val price = getRandomPrice(carPriceType)
        val condition = getRandomCondition()
        val boughtFor = -1L
        val carState = AssetState.MARKET
        val car = Asset.Car(Asset.getNextId(), carName, price, condition, boughtFor, carState, CarType.NORMAL, carIcon)
        return car
    }

    fun getRandomCarName(carType: PriceCategory): Pair<String, Int> {
        val carNames = mapOf(
            PriceCategory.CHEAP to listOf(
                Pair("Compact Car", R.drawable.car_cheap_1),
                Pair("Economy Car", R.drawable.car_cheap_2),
                Pair("Hatchback", R.drawable.car_cheap_2),
                // Add more cheap car names and their icons
            ),
            PriceCategory.MEDIUM to listOf(
                Pair("Sedan", R.drawable.car_medium_1),
                Pair("Crossover", R.drawable.car_medium_1),
                Pair("Sports Car", R.drawable.car_medium_1),
                // Add more medium car names and their icons
            ),
            PriceCategory.HIGH to listOf(
                Pair("Luxury Sedan", R.drawable.car_high_1),
                Pair("SUV", R.drawable.car_high_2),
                Pair("Convertible", R.drawable.car_high_2),
                // Add more high car names and their icons
            ),
            PriceCategory.LUXURY to listOf(
                Pair("Supercar", R.drawable.car_luxury_1),
                Pair("Luxury SUV", R.drawable.car_luxury_1),
                Pair("Sports Convertible", R.drawable.car_luxury_1),
                // Add more luxury car names and their icons
            )
        )
        val carList = carNames[carType] ?: error("Invalid car type")
        return carList.random()
    }

    fun getRandomPrice(carType: PriceCategory): Double {
        return when (carType) {
            PriceCategory.CHEAP -> (10000..30000).random().toDouble()
            PriceCategory.MEDIUM -> (30000..80000).random().toDouble()
            PriceCategory.HIGH -> (80000..150000).random().toDouble()
            PriceCategory.LUXURY -> (150000..500000).random().toDouble()
        }
    }

    fun getRandomCondition(): Int {
        val minCondition = 0
        val maxCondition = 100
        return (minCondition..maxCondition).random()
    }
}