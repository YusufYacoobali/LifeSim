package com.sim.lifesim4.jobs

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.lifesim4.R
import com.sim.lifesim4.models.GameEngine
import com.sim.lifesim4.models.GameEngine.*
import com.sim.lifesim4.models.Job
import com.sim.lifesim4.models.JobType
import com.sim.lifesim4.models.Person
import com.sim.lifesim4.tools.Tools
import kotlin.random.Random

class PartTimeActivity : AppCompatActivity() {

    private lateinit var gameEngine: GameEngine
    private lateinit var player: Person

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.job_part_time)
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

    private fun updatePage(jobs: List<Job.PartTimeJob>, myContract: ActivityResultLauncher<Intent>) {
        val jobContainer = findViewById<LinearLayout>(R.id.jobMarket)
        jobContainer.removeAllViews()

        val cards = Tools.addCardsToView(this, jobs, jobContainer, "sq ft  Condition%", R.drawable.heart, null, myContract)

        cards.forEach { card ->
            val job = card.obj
            val captionTextView: TextView = card.personCard.findViewById(R.id.caption)
            captionTextView.text = "${job.type}"
            card.personCard.setOnClickListener {
                //if they clicked, then they applied, 40% chance of getting it
                if (Random.nextDouble() < 0.4){
                    gameEngine.sendMessage(Message("You started as a part time ${job.type}", false))
                    player.startJob(job)
                    setResult(Activity.RESULT_OK)
                    finish()
                } else {
                    Tools.showPopupDialog(
                        this, "Rejected from job. They didn't like you", "OK", "", job) { resultCode, button -> }
                }
            }
        }
    }

    private fun makeJobs(): List<Job.PartTimeJob> {
        val jobCounts = mapOf(
            JobType.Retail to Random.nextInt(1, 3),
            JobType.Waiter to Random.nextInt(1, 4),
            JobType.Barista to Random.nextInt(1, 3),
            JobType.Tutor to Random.nextInt(1, 3),
            JobType.Babysitter to Random.nextInt(1, 3),
            JobType.DogWalker to Random.nextInt(1, 3)
        )

        val jobs = mutableListOf<Job.PartTimeJob>()

        jobCounts.forEach { (jobType, count) ->
            repeat(count) {
                jobs += makePartTimeJob(jobType)
            }
        }
        return jobs
    }

    private fun makePartTimeJob(jobType: JobType): Job.PartTimeJob {
        val (jobName, jobIcon) = getRandomPartTimeJobName(jobType)
        val salary = getRandomSalary(jobType)
        val hoursPerWeek = getRandomHoursPerWeek()
        return Job.PartTimeJob(Job.getNextId(), jobName, salary, jobType, jobIcon, hoursPerWeek)
    }

    private fun getRandomPartTimeJobName(jobType: JobType): Pair<String, Int> {
        return when (jobType) {
            JobType.Retail -> {
                val jobNames = listOf(
                    "Retail Associate",
                    "Sales Clerk",
                    "Store Assistant",
                    "Customer Service Representative"
                )
                val randomName = jobNames.random()
                Pair(randomName, R.drawable.retail)
            }
            JobType.Waiter -> {
                val jobNames = listOf(
                    "Waiter",
                    "Waitress",
                    "Server",
                    "Restaurant Staff"
                )
                val randomName = jobNames.random()
                Pair(randomName, R.drawable.dining)
            }
            JobType.Barista -> {
                val jobNames = listOf(
                    "Barista",
                    "Coffee Specialist",
                    "Café Attendant"
                )
                val randomName = jobNames.random()
                Pair(randomName, R.drawable.waiter)
            }
            JobType.Tutor -> {
                val jobNames = listOf(
                    "Tutor",
                    "Academic Coach",
                    "Educational Assistant"
                )
                val randomName = jobNames.random()
                Pair(randomName, R.drawable.study)
            }
            JobType.Babysitter -> {
                val jobNames = listOf(
                    "Babysitter",
                    "Childcare Provider",
                    "Nanny"
                )
                val randomName = jobNames.random()
                Pair(randomName, R.drawable.baby)
            }
            JobType.DogWalker -> {
                val jobNames = listOf(
                    "Dog Walker",
                    "Pet Sitter",
                    "Animal Caretaker"
                )
                val randomName = jobNames.random()
                Pair(randomName, R.drawable.pet)
            }
            // Add more job names and icons for other part-time job types
            else -> {Pair("Invalid",-1)}
        }
    }

    private fun getRandomSalary(jobType: JobType): Double {
        return when (jobType) {
            JobType.Retail -> (8..11).random().toDouble()
            JobType.Waiter -> (7..15).random().toDouble()
            JobType.Barista -> (5..11).random().toDouble()
            JobType.Tutor -> (5..11).random().toDouble()
            JobType.Babysitter -> (5..11).random().toDouble()
            JobType.DogWalker -> (5..11).random().toDouble()
            // Assign appropriate salary ranges for other part-time job types
            else -> {-1.00}
        }
    }

    private fun getRandomHoursPerWeek(): Int {
        return (10..30).random()
    }
}