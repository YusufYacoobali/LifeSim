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
import kotlin.random.Random

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
            if (person.isAlive) {
                val clickListener = View.OnClickListener { view ->
                    when (view.id) {
                        R.id.spendTime -> {
                            //player.relationOption(1)
                            Tools.showPopupDialog(
                                this,
                                "You spent time with ${person.name}",
                                "OK",
                                "",
                                null,
                                { resultCode, button -> })
                            person.affection =
                                (person.affection + Random.nextInt(0, 4)).coerceAtMost(100)
                        }

                        R.id.askMoney -> {
                            //player.relationOption(1)
                            val chance = Random.nextInt(0, 101)
                            if (chance < player.fortune && person.money > 0) {
                                val personMaxCash = person.money
                                val cash = Random.nextInt(1, (personMaxCash * 0.1).toInt())
                                person.money -= cash
                                player.money += cash
                                Tools.showPopupDialog(
                                    this,
                                    "You got ${Tools.formatMoney(cash.toLong())}",
                                    "OK",
                                    "",
                                    null,
                                    { resultCode, button -> })
                            } else {
                                Tools.showPopupDialog(
                                    this,
                                    "${person.name} didn't want to give you anything",
                                    "OK",
                                    "Hate them",
                                    null,
                                    { resultCode, button -> })
                            }
                        }

                        R.id.giveMoney -> {
                            val cash = Random.nextInt(1, (player.money * 0.1).toInt())
                            person.money += cash
                            player.money -= cash
                            Tools.showPopupDialog(
                                this,
                                "You gave ${Tools.formatMoney(cash.toLong())} to ${person.name}",
                                "OK",
                                "",
                                null,
                                { resultCode, button -> })
                        }

                        R.id.insult -> {
                            Tools.showPopupDialog(
                                this,
                                "You spoke obscene sentences which resulted in ${person.name} going into depression",
                                "OK",
                                "",
                                null,
                                { resultCode, button -> })
                            person.affection -= Random.nextInt(3, 10)
                        }

                        R.id.compliment -> {
                            Tools.showPopupDialog(
                                this,
                                "You complimented ${person.name}",
                                "OK",
                                "",
                                null,
                                { resultCode, button -> })
                            person.affection += Random.nextInt(0, 4)
                        }

                        R.id.romantic -> {
                            person.affection += Random.nextInt(-6, 5)
                            Tools.showPopupDialog(
                                this,
                                "You had a romantic time with ${person.name}",
                                "OK",
                                "",
                                null,
                                { resultCode, button -> })

                        }

                        R.id.propose -> {
                            if (person.affection > Random.nextInt(50, 80)) {
                                person.affection += Random.nextInt(3, 9)
                                person.affectionType = AffectionType.Wife
                                Tools.showPopupDialog(
                                    this,
                                    "${person.name} accepted your proposal",
                                    "OK",
                                    "",
                                    null,
                                    { resultCode, button -> })
                            } else {
                                person.affection += Random.nextInt(-7, -2)
                                Tools.showPopupDialog(
                                    this,
                                    "${person.name} declined your proposal",
                                    "OK",
                                    "Cry",
                                    null,
                                    { resultCode, button -> })
                            }
                        }

                        R.id.kill -> {
                            //player.relationOption(1)
                            //sendMessage(0, "", "Nice! You got away with killing ${person.name}")
                            if (player.fortune > Random.nextInt(50, 80)) {
                                person.isAlive = false
                                person.affectionType = AffectionType.Dead
                                player.money += (person.money * 0.5).toLong()
                                Tools.showPopupDialog(
                                    this,
                                    "You got away with killing ${person.name} and took half their wealth",
                                    "PARTY!!!",
                                    "",
                                    null,
                                    { resultCode, button -> })
                            } else {
                                person.affection = -100
                                Tools.showPopupDialog(
                                    this,
                                    "You failed killing ${person.name} due to hesitation but they didnt report you because they care about you",
                                    "Slap Yourself",
                                    "",
                                    null,
                                    { resultCode, button -> })
                            }
                        }
                    }
                    setResult(Activity.RESULT_OK)
                    //finish()
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
                Tools.showPopupDialog(
                    this,
                    "${person.name} is dead",
                    "Mourn",
                    "Laugh",
                    null,
                    { resultCode, button -> })
            }

        } else {
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