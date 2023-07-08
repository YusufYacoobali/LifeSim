package com.example.lifesim4.jobs

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.lifesim4.R
import com.example.lifesim4.models.CrimeType
import com.example.lifesim4.models.GameEngine
import com.example.lifesim4.models.Job
import com.example.lifesim4.models.JobType
import com.example.lifesim4.models.Person
import com.example.lifesim4.tools.Tools
import kotlin.random.Random

class CrimeActivity : AppCompatActivity() {
    private lateinit var gameEngine: GameEngine
    private lateinit var player: Person

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.job_crime)
        gameEngine = GameEngine.getInstance()
        player = gameEngine.getPlayer()

        val myContract = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                //setResult(Activity.RESULT_OK)
                finish()
            }
        }
        val jobs = makeJobs().sortedByDescending { it.salary }
        updatePage(jobs, myContract)
    }

    private fun updatePage(jobs: List<Job.Crime>, myContract: ActivityResultLauncher<Intent>) {
        val jobContainer = findViewById<LinearLayout>(R.id.jobMarket)
        jobContainer.removeAllViews()

        val cards = Tools.addCardsToView(this, jobs, jobContainer, "sq ft  Condition%", R.drawable.heart, null, myContract)

        cards.forEach { card ->
            val job = card.obj as Job.Crime
            val captionTextView: TextView = card.personCard.findViewById(R.id.caption)
            captionTextView.text = "${job.crimeType}"
            card.personCard.setOnClickListener {
               //are you sure, if yes then do it with chance of prison
                    Tools.showPopupDialog(
                        this, "Rejected from job. Lacking experience", "OK", "", job) { resultCode, button -> }

            }
        }
    }

    private fun makeJobs(): List<Job.Crime> {
        val jobCounts = mapOf(
            CrimeType.Robbery to Random.nextInt(1, 3),
            CrimeType.Fraud to Random.nextInt(1, 3),
            CrimeType.DrugTrafficking to Random.nextInt(1, 3),
            CrimeType.MoneyLaundering to Random.nextInt(1, 3),
            CrimeType.Kidnapping to Random.nextInt(1, 3),
            CrimeType.CyberCrime to Random.nextInt(1, 3),
            CrimeType.Forgery to Random.nextInt(1, 3),
            CrimeType.Assassination to Random.nextInt(1, 3),
            CrimeType.Smuggling to Random.nextInt(1, 3)
        )

        val jobs = mutableListOf<Job.Crime>()

        jobCounts.forEach { (crimeType, count) ->
            repeat(count) {
                jobs += makeCrimeJob(crimeType)
            }
        }
        return jobs
    }


    private fun makeCrimeJob(crimeType: CrimeType): Job.Crime {
        val (jobName, jobIcon) = getRandomCrimeJobName(crimeType)
        val salary = getRandomPayout(crimeType)
        val popularity = getRandomPopularity()
        val successRate = getRandomSuccessRate()
        return Job.Crime(Job.getNextId(), jobName, salary, JobType.Criminal, jobIcon, popularity, crimeType, successRate)
    }

    private fun getRandomCrimeJobName(crimeType: CrimeType): Pair<String, Int> {
        return when (crimeType) {
            CrimeType.Robbery -> {
                val jobNames = listOf(
                    "Bank Robbery",
                    "Jewelry Store Heist",
                    "Art Gallery Theft"
                )
                val randomName = jobNames.random()
                Pair(randomName, R.drawable.fight)
            }
            CrimeType.Fraud -> {
                val jobNames = listOf(
                    "Credit Card Fraud",
                    "Identity Theft",
                    "Ponzi Scheme"
                )
                val randomName = jobNames.random()
                Pair(randomName, R.drawable.fraud)
            }
            CrimeType.DrugTrafficking -> {
                val jobNames = listOf(
                    "Cocaine Trafficking",
                    "Heroin Distribution",
                    "Marijuana Smuggling"
                )
                val randomName = jobNames.random()
                Pair(randomName, R.drawable.drugs)
            }
            CrimeType.MoneyLaundering -> {
                val jobNames = listOf(
                    "Money Laundering Operation",
                    "Illegal Gambling",
                    "Tax Evasion Scheme"
                )
                val randomName = jobNames.random()
                Pair(randomName, R.drawable.launder)
            }
            CrimeType.Kidnapping -> {
                val jobNames = listOf(
                    "Ransom Kidnapping",
                    "Abduction",
                    "Human Trafficking"
                )
                val randomName = jobNames.random()
                Pair(randomName, R.drawable.baby)
            }
            CrimeType.CyberCrime -> {
                val jobNames = listOf(
                    "Hacking",
                    "Phishing Scam",
                    "Data Breach"
                )
                val randomName = jobNames.random()
                Pair(randomName, R.drawable.laptop)
            }
            CrimeType.Forgery -> {
                val jobNames = listOf(
                    "Fake Signatures",
                    "Phishing Scam",
                    "Data Breach"
                )
                val randomName = jobNames.random()
                Pair(randomName, R.drawable.study)
            }
            CrimeType.Assassination -> {
                val jobNames = listOf(
                    "Kill targets",
                    "Kill and earn Life Insurance",
                    "Kill and consume Bank Account"
                )
                val randomName = jobNames.random()
                Pair(randomName, R.drawable.crime)
            }
            CrimeType.Smuggling -> {
                val jobNames = listOf(
                    "Smuggle Children",
                    "Smuggle Drugs",
                    "Smuggle Immigrants"
                )
                val randomName = jobNames.random()
                Pair(randomName, R.drawable.artist)
            }
            // Add more job names and icons for other crime types
            else -> Pair("Invalid", -1)
        }
    }

    private fun getRandomPayout(crimeType: CrimeType): Double {
        return when (crimeType) {
            CrimeType.Robbery -> (10000..50000).random().toDouble()
            CrimeType.Fraud -> (5000..30000).random().toDouble()
            CrimeType.DrugTrafficking -> (20000..80000).random().toDouble()
            CrimeType.MoneyLaundering -> (10000..50000).random().toDouble()
            CrimeType.Kidnapping -> (50000..200000).random().toDouble()
            CrimeType.CyberCrime -> (10000..50000).random().toDouble()
            CrimeType.Forgery -> (10000..50000).random().toDouble()
            CrimeType.Assassination -> (50000..200000).random().toDouble()
            CrimeType.Smuggling -> (10000..50000).random().toDouble()
            // Assign appropriate payout ranges for other crime types
            else -> -1.00
        }
    }

    private fun getRandomPopularity(): Int {
        return (10..90).random()
    }

    private fun getRandomSuccessRate(): Double {
        return (10..90).random() / 100.0
    }

}