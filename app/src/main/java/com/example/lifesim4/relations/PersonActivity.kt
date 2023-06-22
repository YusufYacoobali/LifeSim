package com.example.lifesim4.relations

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import com.example.lifesim4.R
import com.example.lifesim4.models.Character
import com.example.lifesim4.models.GameEngine
import com.example.lifesim4.models.NPC
import com.example.lifesim4.models.Person
import com.example.lifesim4.tools.Tools

class PersonActivity : AppCompatActivity() {

    private lateinit var gameEngine: GameEngine
    private lateinit var person: Character

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.relation_person)
        gameEngine = GameEngine.getInstance()

        val personName = intent.getStringExtra("ObjectName")
        if (personName != null) {
            person = gameEngine.getPerson(personName)!!
            if (person == null) {
                gameEngine.sendMessage("No person found")
            } else {
                updateUI()
//                buttonTextView.setOnClickListener{
//                    //gameEngine.simulate()
//                    player.money -= 432
//                    player.children[0].money += 100
//                    //buttonTextView.text = "${person.name} ${player.children[0].money}"
//                    setResult(Activity.RESULT_OK)
//                    //finish()
//                }
            }
        } else {
            gameEngine.sendMessage("Invalid person name")
        }

    }

    private fun updateUI(){

        val name = findViewById<TextView>(R.id.name)
        val relationship = findViewById<TextView>(R.id.relationship)
        val age = findViewById<TextView>(R.id.person_age)
        val money = findViewById<TextView>(R.id.money)
        val fame = findViewById<TextView>(R.id.fame)
        val job = findViewById<TextView>(R.id.job)
        val affection = findViewById<TextView>(R.id.affection)

        name.text = person.name
        relationship.text = person.affectionType.toString()
        age.text = "Age: " + person.age.toString()
        money.text = "Money: " + Tools.formatMoney(person.money)
        fame.text = "Fame: " + person.fame.toString()
        job.text = if (person.job == null) "Job: Unemployed" else "Job: " + person.job.toString()
        affection.text = "Affection to you: " + person.affection.toString()

    }
}