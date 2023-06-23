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
import com.example.lifesim4.models.Character
import com.example.lifesim4.models.GameEngine
import com.example.lifesim4.tools.Tools

class AssetActivity : AppCompatActivity() {

    private lateinit var gameEngine: GameEngine
    private lateinit var asset: Asset
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.owned_asset)
        gameEngine = GameEngine.getInstance()

        val assetName = intent.getStringExtra("ObjectName")
        if (assetName != null) {
            asset = gameEngine.getAsset(assetName)!!
            updateUI()

//            val askMoneyOption: LinearLayout = findViewById(R.id.askMoney)
//            val clickListener = View.OnClickListener { view ->
//                when (view.id) {
//                    R.id.askMoney -> {
//                        //gameEngine.askMoney()
//                        gameEngine.sendMessage("Asked money from ${person.affectionType.toString().lowercase()}")
//                    }
//                }
//                setResult(Activity.RESULT_OK)
//                //finish()
//            }
//
//            askMoneyOption.setOnClickListener(clickListener)
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
        val image: ImageView = findViewById<ImageView>(R.id.asset_icon)

        name.text = asset.name
        boughtValue.text = "Bought For: " + Tools.formatMoney(asset.boughtFor)
        curValue.text = "Current Value: " + Tools.formatMoney(asset.value.toLong())
        condition.text = "Condition: " + asset.condition


        if (asset is Asset.House){
            state.text = (asset as Asset.House).state.toString()
        }
        else if (asset is Asset.Car){
            image.setImageResource(R.drawable.buy_car)
            state.text = (asset as Asset.Car).state.toString()
        }
//        name.text = person.name
//        relationship.text = person.affectionType.toString()
//        age.text = "Age: " + person.age.toString()
//        money.text = "Money: " + Tools.formatMoney(person.money)
//        fame.text = "Fame: " + person.fame.toString()
//        job.text = if (person.job == null) "Job: Unemployed" else "Job: " + person.job.toString()
//        affection.text = "Affection to you: " + person.affection.toString()

    }
}