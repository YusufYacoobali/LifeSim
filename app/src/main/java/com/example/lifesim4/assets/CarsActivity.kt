package com.example.lifesim4.assets

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.lifesim4.R
import com.example.lifesim4.models.Asset
import com.example.lifesim4.models.CarState
import com.example.lifesim4.models.GameEngine
import com.example.lifesim4.models.HouseState
import com.example.lifesim4.models.Person

class CarsActivity : AppCompatActivity() {
    private lateinit var gameEngine: GameEngine
    private lateinit var player: Person
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.owned_cars)
        gameEngine = GameEngine.getInstance()
        player = gameEngine.getPlayer()

        val currentContainer: LinearLayout = findViewById(R.id.current)
        val allHomesContainer: LinearLayout = findViewById(R.id.allCars)

        val currentCar = player.assets.find { asset ->
            asset is Asset.Car && asset.state == CarState.PRIMARY
        } as? Asset.Car

        val allCars = player.assets.filterIsInstance<Asset.Car>().filter { it.state != CarState.PRIMARY}

        if (currentCar != null) {
            addCarToView(currentContainer, currentCar.name , "Condition ${currentCar.condition}%", R.drawable.buy_car)
        }

        allCars.forEach { car ->
            addCarToView(allHomesContainer, car.name , "${car.state}  Condition ${car.condition}%", R.drawable.buy_car)
        }
    }

    private fun addCarToView(placement: LinearLayout, name: String?, caption: String, icon: Int){
        // Create an instance of the card_basic layout
        val personCard = layoutInflater.inflate(R.layout.card_basic, placement, false)

        // Find the views inside the fatherCard layout and set the father's details
        val nameTextView: TextView = personCard.findViewById(R.id.name)
        val captionTextView: TextView = personCard.findViewById(R.id.caption)
        val image: ImageView = personCard.findViewById(R.id.image)

        nameTextView.text = name
        captionTextView.text = caption
        image.setImageResource(icon)
        placement.addView(personCard)
        //return personCard
    }
}