package com.sim.lifesim4.relations

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.example.lifesim4.R
import com.sim.lifesim4.models.AffectionType
import com.sim.lifesim4.models.Character
import com.sim.lifesim4.models.GameEngine
import com.sim.lifesim4.models.GameEngine.*
import com.sim.lifesim4.models.Person
import com.sim.lifesim4.tools.Tools

class PersonActivity : AppCompatActivity() {

    private lateinit var gameEngine: GameEngine
    private lateinit var player: Person
    private lateinit var person: Character

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.relation_person)
        gameEngine = GameEngine.getInstance()
        player = gameEngine.getPlayer()

        val spendTime: LinearLayout = findViewById(R.id.spendTime)
        val askMoney: LinearLayout = findViewById(R.id.askMoney)
        val giveMoney: LinearLayout = findViewById(R.id.giveMoney)
        val insult: LinearLayout = findViewById(R.id.insult)
        val compliment: LinearLayout = findViewById(R.id.compliment)
        val romantic: LinearLayout = findViewById(R.id.romantic)
        val propose: LinearLayout = findViewById(R.id.propose)
        val kill: LinearLayout = findViewById(R.id.kill)
        romantic.visibility = View.GONE

        val personName = intent.getStringExtra("ObjectName")
        if (personName != null) {
            person = gameEngine.getPerson(personName)!!
            updateUI()

            if (person.affectionType != AffectionType.Girlfriend){
                propose.visibility = View.GONE
            }
            if (person.affectionType == AffectionType.Girlfriend || person.affectionType == AffectionType.Wife) {
                romantic.visibility = View.VISIBLE
            }
            //hide certain buttons based on relation eg to family, hide propose

            val clickListener = View.OnClickListener { view ->
                when (view.id) {
                    R.id.spendTime -> {
                        //player.relationOption(1)
                        sendMessage(0, "", "You spent time with ${person.name}")
                    }
                    R.id.askMoney -> {
                        //player.relationOption(1)
                        sendMessage(0, "", "You got $")
                    }
                    R.id.giveMoney -> {
                        //player.relationOption(1)
                        sendMessage(0, "", "You gave $")
                    }
                    R.id.insult -> {
                        //player.relationOption(1)
                        sendMessage(0, "", "You spoke obscene sentences which resulted in ${person.name} going into depression")
                    }
                    R.id.compliment -> {
                        //player.relationOption(1)
                        sendMessage(0, "", "You complimented ${person.name}")
                    }
                    R.id.romantic -> {
                        //player.relationOption(1)
                        sendMessage(0, "", "You had a romantic time with ${person.name}")
                    }
                    R.id.propose -> {
                        //player.relationOption(1)
                        sendMessage(0, "", "${person.name} accepted your proposal")
                    }
                    R.id.kill -> {
                        //player.relationOption(1)
                        sendMessage(0, "", "Nice! You got away with killing ${person.name}")
                    }
                }
                setResult(Activity.RESULT_OK)
                finish()
            }
            spendTime.setOnClickListener(clickListener)
            askMoney.setOnClickListener(clickListener)
            giveMoney.setOnClickListener(clickListener)
            insult.setOnClickListener(clickListener)
            compliment.setOnClickListener(clickListener)
            romantic.setOnClickListener(clickListener)
            propose.setOnClickListener(clickListener)
            kill.setOnClickListener(clickListener)
        } else {
            //gameEngine.sendMessage("Invalid person name")
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

    fun sendMessage(charge: Long, minCost: String, successMessage: String) {
        if (charge == -1L){
            gameEngine.sendMessage(
                GameEngine.Message(
                    "Minimum charge is $${minCost}. You cant afford this holiday.",
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