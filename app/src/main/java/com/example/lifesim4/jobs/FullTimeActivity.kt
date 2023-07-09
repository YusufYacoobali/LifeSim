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
import com.example.lifesim4.models.Person
import com.example.lifesim4.tools.Tools
import kotlin.random.Random

class FullTimeActivity : AppCompatActivity() {
    private lateinit var gameEngine: GameEngine
    private lateinit var player: Person

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.job_full_time)
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

    private fun updatePage(jobs: List<Job.FullTimeJob>, myContract: ActivityResultLauncher<Intent>) {
        val jobContainer = findViewById<LinearLayout>(R.id.jobMarket)
        jobContainer.removeAllViews()

        val cards = Tools.addCardsToView(this, jobs, jobContainer, "sq ft  Condition%", R.drawable.heart, null, myContract)

        cards.forEach { card ->
            val job = card.obj as Job.FullTimeJob
            val captionTextView: TextView = card.personCard.findViewById(R.id.caption)
            captionTextView.text = "${job.type}"
            card.personCard.setOnClickListener {
                //if they clicked, then they applied
                if (player.isEligibleForJob(job)){
                    gameEngine.sendMessage(GameEngine.Message("Congrats you got the job", false))
                    player.startJob(job)
                    setResult(Activity.RESULT_OK)
                    finish()
                } else {
                    Tools.showPopupDialog(
                        this, "Rejected from job. Lacking experience", "OK", "", job) { resultCode, button -> }
                }
            }
        }
    }

    private fun makeJobs(): List<Job.FullTimeJob> {
        val jobCounts = mapOf(
            JobLevel.Entry to Random.nextInt(1, 3),
            JobLevel.Normal to Random.nextInt(1, 4),
            JobLevel.Manager to Random.nextInt(1, 3),
            JobLevel.Specialist to Random.nextInt(0, 3),
            JobLevel.Senior to Random.nextInt(1, 3),
            JobLevel.Director to Random.nextInt(0, 3),
            JobLevel.Executive to Random.nextInt(0, 3)
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

    private fun getRandomFullTimeJobName(jobLevel: JobLevel): Triple<String, Int, JobType> {
        val fullTimeJobs = when (jobLevel) {
            JobLevel.Entry -> listOf(
                Triple("Junior Developer", R.drawable.laptop, JobType.Programmer),
                Triple("Data Analyst", R.drawable.money, JobType.Finance),
                Triple("Marketing Assistant", R.drawable.marketing, JobType.Marketing),
                Triple("Teacher Assistant", R.drawable.teacher, JobType.Teacher),
                Triple("Kitchen Assistant", R.drawable.dining, JobType.Chef),
                Triple("Junior Engineer", R.drawable.engineer, JobType.Engineer),
                Triple("Medical Intern", R.drawable.doctor, JobType.Doctor),
                Triple("Junior Artist", R.drawable.artist, JobType.Artist)
            )
            JobLevel.Normal -> listOf(
                Triple("Software Engineer", R.drawable.laptop, JobType.Programmer),
                Triple("Marketing Specialist", R.drawable.marketing, JobType.Marketing),
                Triple("Financial Analyst", R.drawable.money, JobType.Finance),
                Triple("Sales Representative", R.drawable.marketing, JobType.Marketing),
                Triple("Teacher", R.drawable.teacher, JobType.Teacher),
                Triple("Chef", R.drawable.dining, JobType.Chef),
                Triple("Engineer", R.drawable.engineer, JobType.Engineer),
                Triple("Doctor", R.drawable.doctor, JobType.Doctor),
                Triple("Artist", R.drawable.artist, JobType.Artist)
            )
            JobLevel.Manager -> listOf(
                Triple("Development Manager", R.drawable.laptop, JobType.Programmer),
                Triple("Marketing Manager", R.drawable.marketing, JobType.Marketing),
                Triple("Finance Manager", R.drawable.money, JobType.Finance),
                Triple("Sales Manager", R.drawable.marketing, JobType.Marketing),
                Triple("School Principal", R.drawable.teacher, JobType.Teacher),
                Triple("Head Chef", R.drawable.dining, JobType.Chef),
                Triple("Engineering Manager", R.drawable.engineer, JobType.Engineer),
                Triple("Medical Director", R.drawable.doctor, JobType.Doctor),
                Triple("Art Manager", R.drawable.artist, JobType.Artist)
            )
            JobLevel.Specialist -> listOf(
                Triple("Software Architect", R.drawable.laptop, JobType.Programmer),
                Triple("Marketing Strategist", R.drawable.marketing, JobType.Marketing),
                Triple("Senior Financial Analyst", R.drawable.money, JobType.Finance),
                Triple("Business  Specialist", R.drawable.marketing, JobType.Marketing),
                Triple("Professor", R.drawable.teacher, JobType.Teacher),
                Triple("Master Chef", R.drawable.dining, JobType.Chef),
                Triple("Senior Engineer", R.drawable.engineer, JobType.Engineer),
                Triple("Surgeon", R.drawable.doctor, JobType.Doctor),
                Triple("Fine Artist", R.drawable.artist, JobType.Artist)
            )
            JobLevel.Senior -> listOf(
                Triple("Senior Software Engineer", R.drawable.laptop, JobType.Programmer),
                Triple("Marketing Manager", R.drawable.marketing, JobType.Marketing),
                Triple("Senior Financial Analyst", R.drawable.money, JobType.Finance),
                Triple("Sales Manager", R.drawable.marketing, JobType.Marketing),
                Triple("Senior Teacher", R.drawable.teacher, JobType.Teacher),
                Triple("Executive Chef", R.drawable.dining, JobType.Chef),
                Triple("Senior Engineer", R.drawable.engineer, JobType.Engineer),
                Triple("Senior Doctor", R.drawable.doctor, JobType.Doctor),
                Triple("Expert Art Examiner", R.drawable.artist, JobType.Artist)
            )
            JobLevel.Director -> listOf(
                Triple("Director of Engineering", R.drawable.laptop, JobType.Programmer),
                Triple("Chief Financial Officer", R.drawable.money, JobType.Finance),
                Triple("Chief Technology Officer", R.drawable.laptop, JobType.Programmer),
                Triple("Chief Marketing Officer", R.drawable.marketing, JobType.Marketing),
                Triple("School Director", R.drawable.teacher, JobType.Teacher),
                Triple("Superintendent", R.drawable.teacher, JobType.Teacher),
                Triple("Head Chef", R.drawable.dining, JobType.Chef),
                Triple("Engineering Director", R.drawable.engineer, JobType.Engineer),
                Triple("Chief Medical Officer", R.drawable.doctor, JobType.Doctor),
                Triple("Creative Arts Director", R.drawable.artist, JobType.Artist)
            )
            JobLevel.Executive -> listOf(
                Triple("Chief Executive Officer", R.drawable.money, JobType.Finance),
                Triple("Chief Medical Officer", R.drawable.doctor, JobType.Doctor),
            )
        }
        return fullTimeJobs.random()
    }

    private fun getRandomSalary(jobLevel: JobLevel): Double {
        val salary = when (jobLevel) {
            JobLevel.Entry -> (20000..70000).random().toDouble()
            JobLevel.Normal -> (70000..130000).random().toDouble()
            JobLevel.Manager -> (130000..180000).random().toDouble()
            JobLevel.Specialist -> (180000..250000).random().toDouble()
            JobLevel.Senior -> (250000..350000).random().toDouble()
            JobLevel.Director -> (350000..10000000).random().toDouble()
            JobLevel.Executive -> (10000000..20000000).random().toDouble()
        }
        return (salary / 1000).toInt() * 1000.toDouble()
    }
}