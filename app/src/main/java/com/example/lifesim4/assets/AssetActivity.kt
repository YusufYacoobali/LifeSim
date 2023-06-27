package com.example.lifesim4.assets

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.lifesim4.R
import com.example.lifesim4.models.Asset
import com.example.lifesim4.models.GameEngine
import com.example.lifesim4.models.Person
import com.example.lifesim4.tools.Tools

class AssetActivity : AppCompatActivity() {

    private lateinit var gameEngine: GameEngine
    private lateinit var asset: Asset
    private lateinit var player: Person
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.owned_asset)
        gameEngine = GameEngine.getInstance()
        player = gameEngine.getPlayer()

        val assetID = intent.getStringExtra("ObjectID")
        if (assetID != null) {
            asset = gameEngine.getAsset(assetID)!!
            println("tHING: ${asset.boughtFor}.")
            updateUI()

            val conditionButton = findViewById<LinearLayout>(R.id.conditionButton)
//            val askMoneyOption: LinearLayout = findViewById(R.id.askMoney)
            val clickListener = View.OnClickListener { view ->
                when (view.id) {
                    R.id.conditionButton -> {
                        //gameEngine.askMoney()
                        asset.condition = 100
                        player.money -= 10000
                        gameEngine.sendMessage("Asset fixed")
                        updateUI()
                    }
                }
                setResult(Activity.RESULT_OK)
                //finish()
            }

            conditionButton.setOnClickListener(clickListener)
        } else {
            gameEngine.sendMessage("Invalid Asset name")
        }
    }

    private fun updateUI(){

        val name = findViewById<TextView>(R.id.name)
        val boughtValue = findViewById<TextView>(R.id.boughtValue)
        val curValue = findViewById<TextView>(R.id.curValue)
        val condition = findViewById<TextView>(R.id.condition)
        val state = findViewById<TextView>(R.id.state)
        val info = findViewById<TextView>(R.id.info)
        val image: ImageView = findViewById(R.id.asset_icon)
        val rentButton = findViewById<LinearLayout>(R.id.rent)

        name.text = asset.name
        boughtValue.text = "Bought For: " + Tools.formatMoney(asset.boughtFor)
        curValue.text = "Current Value: " + Tools.formatMoney(asset.value.toLong())
        condition.text = "Condition: " + asset.condition

        if (asset is Asset.House){
            state.text = (asset as Asset.House).state.description
            info.text = (asset as Asset.House).squareFeet.toString() + "sq ft"
        }
        else if (asset is Asset.Car){
            image.setImageResource(R.drawable.buy_car)
            state.text = "Type: " + (asset as Asset.Car).state.toString().lowercase()
            info.visibility = View.GONE
            rentButton.visibility = View.GONE
        }
        else if (asset is Asset.Boat){
            image.setImageResource(R.drawable.buy_boat)
            state.visibility = View.GONE
            info.visibility = View.GONE
            rentButton.visibility = View.GONE
        }
        else if (asset is Asset.Plane){
            image.setImageResource(R.drawable.buy_planes)
            state.visibility = View.GONE
            info.visibility = View.GONE
            rentButton.visibility = View.GONE
        }
    }
}