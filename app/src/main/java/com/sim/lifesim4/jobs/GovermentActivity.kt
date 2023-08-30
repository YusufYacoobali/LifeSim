package com.sim.lifesim4.jobs

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import com.example.lifesim4.R
import com.sim.lifesim4.models.FameLevel
import com.sim.lifesim4.models.GameEngine
import com.sim.lifesim4.models.Person
import com.sim.lifesim4.tools.Tools

class GovermentActivity : AppCompatActivity() {
    private lateinit var gameEngine: GameEngine
    private lateinit var player: Person

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.job_goverment)
        gameEngine = GameEngine.getInstance()
        player = gameEngine.getPlayer()

        //create the jobs too
        val emperor: LinearLayout = findViewById(R.id.emperor)
        val king: LinearLayout = findViewById(R.id.king)
        val mayor: LinearLayout = findViewById(R.id.mayor)
        val community: LinearLayout = findViewById(R.id.community)
        val rebel: LinearLayout = findViewById(R.id.rebel)

        emperor.setOnClickListener{
            if (player.fame >= FameLevel.A && player.followers > 1000000){
                Tools.showPopupDialog(
                    this, "You are now an Emperor", "OK", "", null) { resultCode, button ->
                    //player.startGovJob()
                    setResult(resultCode)
                    finish()
                }
            }
            else {
                Tools.showPopupDialog(
                    this, "You do not have enough influence or power to become an emperor", "OK", "", null) { resultCode, button ->
                }
            }
        }

        king.setOnClickListener{
            if (player.fame >= FameLevel.A && player.followers > 100000){
                Tools.showPopupDialog(
                    this, "You are now a King", "OK", "", null) { resultCode, button ->
                    //player.startGovJob()
                    setResult(resultCode)
                    finish()
                }
            }
            else {
                Tools.showPopupDialog(
                    this, "You do not have enough influence or power to become a King", "OK", "", null) { resultCode, button ->
                }
            }
        }

        mayor.setOnClickListener{
            if (player.fame >= FameLevel.A && player.followers > 10000){
                Tools.showPopupDialog(
                    this, "You are now a Mayor", "OK", "", null) { resultCode, button ->
                    //player.startGovJob()
                    setResult(resultCode)
                    finish()
                }
            }
            else {
                Tools.showPopupDialog(
                    this, "You do not have enough influence to become the mayor", "OK", "", null) { resultCode, button ->
                }
            }
        }

        community.setOnClickListener{
            if (player.fame >= FameLevel.A && player.followers > 100){
                Tools.showPopupDialog(
                    this, "You are now a community leader", "OK", "", null) { resultCode, button ->
                    //player.startGovJob()
                    setResult(resultCode)
                    finish()
                }
            }
            else {
                Tools.showPopupDialog(
                    this, "You do not have enough influence to lead the community", "OK", "", null) { resultCode, button ->
                }
            }
        }

        rebel.setOnClickListener{
            if (player.fame >= FameLevel.A && player.followers > 1000000){
                Tools.showPopupDialog(
                    this, "You have created a rebel faction", "OK", "", null) { resultCode, button ->
                    //player.startGovJob()
                    setResult(resultCode)
                    finish()
                }
            }
            else {
                Tools.showPopupDialog(
                    this, "You do not have enough influence or power to start a rebellion", "OK", "", null) { resultCode, button ->
                }
            }
        }
    }
}