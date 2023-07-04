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
import com.example.lifesim4.models.Asset
import com.example.lifesim4.models.AssetState
import com.example.lifesim4.models.GameEngine
import com.example.lifesim4.models.Job
import com.example.lifesim4.models.JobType
import com.example.lifesim4.tools.Tools
import kotlin.random.Random

class FullTimeActivity : AppCompatActivity() {
    private lateinit var gameEngine: GameEngine

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.job_full_time)
        gameEngine = GameEngine.getInstance()

        val myContract = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                //setResult(Activity.RESULT_OK)
                finish()
            }
        }
        val jobs = makeJobs().sortedByDescending { it.salary }
        updatePage(jobs, myContract)
    }

    private fun updatePage(jobs: List<Job.FullTimeJob>, myContract: ActivityResultLauncher<Intent>) {
        val jobContainer = findViewById<LinearLayout>(R.id.jobMarket)
        jobContainer.removeAllViews()

        //val marketHouses = jobs.filter { it.state == AssetState.MARKET }
        val cards = Tools.addCardsToView(this, jobs, jobContainer, "sq ft  Condition%", R.drawable.heart, null, myContract)

        cards.forEach { card ->
            val jobs = card.obj as Job.FullTimeJob
            val captionTextView: TextView = card.personCard.findViewById(R.id.caption)
            captionTextView.text = "${jobs.name}%"
            card.personCard.setOnClickListener {
                Tools.showPopupDialog(this, "Would you like to buy this for \n${
                    Tools.formatMoney(
                        jobs.salary.toLong()
                    )
                }", "Rent", "Buy", jobs) { resultCode, button ->
                    if (button == 1){
                    } else if (button == 2){
                    }
                }
            }
        }
    }

    private fun makeJobs(): List<Job.FullTimeJob> {
//        val jobCounts = mapOf(
//            JobType.FullTime to Random.nextInt(2, 5)
//        )

        val jobs = mutableListOf<Job.FullTimeJob>()

        //jobCounts.forEach { (jobType, count) ->
            repeat(4) {
                jobs += makeFullTimeJob()
            }
        //}
        return jobs
    }

    private fun makeFullTimeJob(): Job.FullTimeJob {
        val (jobName, jobIcon) = getRandomFullTimeJobName()
        val salary = getRandomSalary()
        return Job.FullTimeJob(Job.getNextId(), jobName, salary, JobType.Astronaut, jobIcon, 1)
    }

    fun getRandomFullTimeJobName(): Pair<String, Int> {
        val fullTimeJobs = listOf(
            Pair("Software Engineer", R.drawable.heart),
            Pair("Accountant", R.drawable.male),
            Pair("Marketing Manager", R.drawable.money),
            Pair("Sales Representative", R.drawable.home),
            // Add more full-time job names and icons
        )
        return fullTimeJobs.random()
    }


    fun getRandomSalary(): Double {
        return (50000..150000).random().toDouble()
    }
}