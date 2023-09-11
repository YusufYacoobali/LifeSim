package com.sim.lifesim4.activities

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.widget.SwitchCompat
import com.example.lifesim4.R
import com.github.javafaker.Bool
import com.sim.lifesim4.models.GameEngine
import com.sim.lifesim4.models.Person
import com.sim.lifesim4.models.Skill
import com.sim.lifesim4.tools.Tools

class SkillsActivity : AppCompatActivity() {
    private lateinit var gameEngine: GameEngine
    private lateinit var player: Person
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_skills)
        gameEngine = GameEngine.getInstance()
        player = gameEngine.getPlayer()
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        val skills = mapOf(
            Skill.READ to R.id.readSwitch,
            Skill.ACT to R.id.actingSwitch,
            Skill.PRAY to R.id.praySwitch,
            Skill.FIGHT to R.id.fightSwitch,
            Skill.POLITICS to R.id.politicsSwitch,
            Skill.CRIME to R.id.crimeSwitch
        )

        for ((skill, switchId) in skills) {
            val switch: SwitchCompat = findViewById(switchId)
            setupSwitch(switch, "${skill.name}Switch", skill)
        }
    }

    private fun setupSwitch(switch: SwitchCompat, key: String, skill: Skill) {
        // Load and set the state from SharedPreferences
        switch.isChecked = sharedPreferences.getBoolean(key, false)

        switch.setOnClickListener {
            val charge = when (skill) {
                Skill.READ -> 300L
                Skill.ACT -> 2400L
                Skill.PRAY -> 50L
                Skill.FIGHT -> 1100L
                Skill.POLITICS -> 450L
                Skill.CRIME -> 500L
            }

            if (switch.isChecked) {
                val hasCash = checkFinance(charge)
                if (!hasCash) {
                    switch.isChecked = !switch.isChecked
                    return@setOnClickListener
                }
                player.hasSkill(skill) // Call player.hasSkill(skill) when turning the skill on
            } else {
               // player.turnOffSkill(skill.number) // Call player.turnOffSkill(number) when turning the skill off
            }

            // Save the updated state to SharedPreferences
            sharedPreferences.edit().putBoolean(key, switch.isChecked).apply()

            // Update the activity result
            setResult(Activity.RESULT_OK)
        }
    }

    private fun setupSwitch2(switch: SwitchCompat, key: String, skill: Skill) {
        // Load and set the state from SharedPreferences
        switch.isChecked = sharedPreferences.getBoolean(key, false)

        switch.setOnClickListener {
            //if going to start a skill
            if (!switch.isChecked){
                when (skill) {
                    Skill.READ -> {
                        val hasCash = checkFinance(300)
                        if (hasCash) {
                            player.hasSkill(skill)
                        } else {
                            switch.isChecked = !switch.isChecked
                        }
                    }

                    Skill.ACT -> {
                        val hasCash = checkFinance(2400)
                        if (hasCash) {
                            player.hasSkill(skill)
                        } else {
                            switch.isChecked = !switch.isChecked
                        }
                    }

                    Skill.PRAY -> {
                        val hasCash = checkFinance(50)
                        if (hasCash) {
                            player.hasSkill(skill)
                        } else {
                            switch.isChecked = !switch.isChecked
                        }
                    }
                    Skill.FIGHT -> {
                        val hasCash = checkFinance(1100)
                        if (hasCash) {
                            player.hasSkill(skill)
                        } else {
                            switch.isChecked = !switch.isChecked
                        }

                    }
                    Skill.POLITICS -> {
                        val hasCash = checkFinance(450)
                        if (hasCash) {
                            player.hasSkill(skill)
                        } else {
                            switch.isChecked = !switch.isChecked
                        }
                    }

                    Skill.CRIME -> {
                        val hasCash = checkFinance(500)
                        if (hasCash) {
                            player.hasSkill(skill)
                        } else {
                            switch.isChecked = !switch.isChecked
                        }
                    }
                }
            } else {
                //turning skill off

            }
            sharedPreferences.edit().putBoolean(key, switch.isChecked).apply()
            // Update the activity result and finish the activity
            setResult(Activity.RESULT_OK)
            //finish()
        }
    }

    private fun checkFinance(charge: Long) : Boolean {
        return if (player.money < charge){
            Tools.showPopupDialog(
                this, "You cant afford this at the moment", "OK", "", null) { resultCode, button -> }
            false
        } else {
            true
        }
    }

    fun sendMessage(charge: Long, successMessage: String) {
        if (charge == -1L){
            gameEngine.sendMessage(
                GameEngine.Message(
                    "You can't afford to move at this moment",
                    false
                )
            )
        } else {
            gameEngine.sendMessage(
                GameEngine.Message(
                    successMessage, false
                )
            )
        }
    }
}
