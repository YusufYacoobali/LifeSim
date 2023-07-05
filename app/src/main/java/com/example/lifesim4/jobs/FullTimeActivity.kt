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
import com.example.lifesim4.models.JobLevel
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

        val cards = Tools.addCardsToView(this, jobs, jobContainer, "sq ft  Condition%", R.drawable.heart, null, myContract)

        cards.forEach { card ->
            val jobs = card.obj as Job.FullTimeJob
            val captionTextView: TextView = card.personCard.findViewById(R.id.caption)
            captionTextView.text = "${jobs.type}"
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
        val jobCounts = mapOf(
            JobLevel.Entry to Random.nextInt(1,5),
            JobLevel.Normal to Random.nextInt(1, 4),
            JobLevel.Senior to Random.nextInt(1, 3),
            JobLevel.Director to Random.nextInt(0,3)
        )

        val jobs = mutableListOf<Job.FullTimeJob>()

        jobCounts.forEach { (jobLevel, count) ->
            repeat(count) {
                jobs += makeFullTimeJob(jobLevel)
            }
        }
        return jobs
    }

    private fun makeFullTimeJob(jobLevel: JobLevel): Job.FullTimeJob {
        val (jobName, jobIcon, jobType) = getRandomFullTimeJobName(jobLevel)
        val salary = getRandomSalary(jobLevel)
        return Job.FullTimeJob(Job.getNextId(), jobName, salary, jobType, jobIcon, jobLevel)
    }

    fun getRandomFullTimeJobName(jobLevel: JobLevel): Triple<String, Int, JobType> {
        val fullTimeJobs = when (jobLevel) {
            JobLevel.Entry -> listOf(
                Triple("Junior Developer", R.drawable.laptop, JobType.Programmer),
                Triple("Data Analyst", R.drawable.money, JobType.Finance),
                Triple("Marketing Assistant", R.drawable.marketing, JobType.Marketing)
            )
            JobLevel.Normal -> listOf(
                Triple("Software Engineer", R.drawable.laptop, JobType.Programmer),
                Triple("Marketing Specialist", R.drawable.marketing, JobType.Marketing),
                Triple("Financial Analyst", R.drawable.money, JobType.Finance),
                Triple("Sales Representative", R.drawable.marketing, JobType.Marketing)
            )
            JobLevel.Senior -> listOf(
                Triple("Senior Software Engineer", R.drawable.laptop, JobType.Programmer),
                Triple("Marketing Manager", R.drawable.marketing, JobType.Marketing),
                Triple("Senior Financial Analyst", R.drawable.money, JobType.Finance),
                Triple("Sales Manager", R.drawable.marketing, JobType.Marketing)
            )
            JobLevel.Director -> listOf(
                Triple("Director of Engineering", R.drawable.laptop, JobType.Programmer),
                Triple("Chief Marketing Officer", R.drawable.marketing, JobType.Marketing),
                Triple("Chief Financial Officer", R.drawable.money, JobType.Finance),
                Triple("Chief Technology Officer", R.drawable.laptop, JobType.Programmer)
            )
        }
        return fullTimeJobs.random()
    }

    fun getRandomSalary(jobLevel: JobLevel): Double {
        val salary = when (jobLevel) {
            JobLevel.Entry -> (20000..70000).random().toDouble()
            JobLevel.Normal -> (70000..130000).random().toDouble()
            JobLevel.Senior -> (130000..250000).random().toDouble()
            JobLevel.Director -> (250000..20000000).random().toDouble()
        }
        return (salary / 1000).toInt() * 1000.toDouble()
    }

}