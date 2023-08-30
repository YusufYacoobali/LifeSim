package com.sim.lifesim4

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.activity.result.contract.ActivityResultContracts
import com.example.lifesim4.R
import com.sim.lifesim4.assets.BoatActivity
import com.sim.lifesim4.assets.CarsActivity
import com.sim.lifesim4.assets.HouseActivity
import com.sim.lifesim4.assets.PlanesActivity

class AssetActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_asset)

        val myContract = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                setResult(Activity.RESULT_OK)
                //finish()
            }
        }

        val houseButton: LinearLayout = findViewById(R.id.houseButton)
        val carsButton: LinearLayout = findViewById(R.id.carsButton)
        val boatsButton: LinearLayout = findViewById(R.id.boatsButton)
        val planesButton: LinearLayout = findViewById(R.id.planesButton)

        val clickListener = View.OnClickListener { view ->
            when (view.id) {
                R.id.houseButton -> {
                    val intent = Intent(this, HouseActivity::class.java)
                    myContract.launch(intent)
                }
                R.id.carsButton -> {
                    val intent = Intent(this, CarsActivity::class.java)
                    myContract.launch(intent)
                }
                R.id.boatsButton -> {
                    val intent = Intent(this, BoatActivity::class.java)
                    myContract.launch(intent)
                }
                R.id.planesButton -> {
                    val intent = Intent(this, PlanesActivity::class.java)
                    myContract.launch(intent)
                }
            }
        }

        houseButton.setOnClickListener(clickListener)
        carsButton.setOnClickListener(clickListener)
        boatsButton.setOnClickListener(clickListener)
        planesButton.setOnClickListener(clickListener)
    }
}