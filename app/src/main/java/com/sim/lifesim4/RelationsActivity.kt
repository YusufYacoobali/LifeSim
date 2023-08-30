package com.sim.lifesim4

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.activity.result.contract.ActivityResultContracts
import com.example.lifesim4.R
import com.sim.lifesim4.relations.EnemiesActivity
import com.sim.lifesim4.relations.FamilyActivity
import com.sim.lifesim4.relations.FriendsActivity
import com.sim.lifesim4.relations.LoversActivity

class RelationsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.relation_relations)

        val myContract = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                setResult(Activity.RESULT_OK)
                finish()
            }
        }

        val familyButton: LinearLayout = findViewById(R.id.familyButton)
        val loversButton: LinearLayout = findViewById(R.id.loversButton)
        val friendsButton: LinearLayout = findViewById(R.id.friendsButton)
        val enemiesButton: LinearLayout = findViewById(R.id.enemiesButton)

        val clickListener = View.OnClickListener { view ->
            when (view.id) {
                R.id.familyButton -> {
                    val intent = Intent(this, FamilyActivity::class.java)
                    myContract.launch(intent)
                }
                R.id.loversButton -> {
                    val intent = Intent(this, LoversActivity::class.java)
                    myContract.launch(intent)
                }
                R.id.friendsButton -> {
                    val intent = Intent(this, FriendsActivity::class.java)
                    myContract.launch(intent)
                }
                R.id.enemiesButton -> {
                    val intent = Intent(this, EnemiesActivity::class.java)
                    myContract.launch(intent)
                }
            }
        }

        familyButton.setOnClickListener(clickListener)
        loversButton.setOnClickListener(clickListener)
        friendsButton.setOnClickListener(clickListener)
        enemiesButton.setOnClickListener(clickListener)
    }
}