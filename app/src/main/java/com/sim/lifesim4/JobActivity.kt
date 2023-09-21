package com.sim.lifesim4

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.result.contract.ActivityResultContracts
import com.example.lifesim4.R
import com.sim.lifesim4.jobs.CrimeActivity
import com.sim.lifesim4.jobs.EntrepreneurActivity
import com.sim.lifesim4.jobs.FullTimeActivity
import com.sim.lifesim4.jobs.GovermentActivity
import com.sim.lifesim4.jobs.PartTimeActivity
import com.sim.lifesim4.models.GameEngine
import com.sim.lifesim4.models.Job
import com.sim.lifesim4.models.Person
import com.sim.lifesim4.tools.Tools

class JobActivity : AppCompatActivity()  {

    private lateinit var gameEngine: GameEngine
    private lateinit var player: Person
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_job)
        gameEngine = GameEngine.getInstance()
        player = gameEngine.getPlayer()

        //Get data from next screen and pass it to main screen
        val myContract = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                setResult(Activity.RESULT_OK)
                finish()
            }
        }

        val currentWorkLayout: LinearLayout = findViewById(R.id.current)

        if (player.job != null) {
            val currentWork = Tools.addCardToView(this, player.job,  currentWorkLayout, player.job!!.type.toString(), R.drawable.home, null, myContract)
            currentWork.personCard.setOnClickListener {
                Tools.showPopupDialog(
                    this,
                    "What would you like to do?",
                    "Work Harder",
                    "Quit",
                    null
                ) { resultCode, button ->
                    if (button == 2) {
                        val job = currentWork.obj as Job
                        player.leaveJob(job)
                        gameEngine.sendMessage(GameEngine.Message("You left your job", false))
                        finish()
                    }
                }
            }
        }

        //handle different buttons
        val fullTimeJobLayout: LinearLayout = findViewById(R.id.fullTimeJobLayout)
        fullTimeJobLayout.setOnClickListener {
            val intent = Intent(this, FullTimeActivity::class.java)
            myContract.launch(intent)
        }

        val partTimeJobLayout: LinearLayout = findViewById(R.id.partTimeJobLayout)
        partTimeJobLayout.setOnClickListener {
            val intent = Intent(this, PartTimeActivity::class.java)
            myContract.launch(intent)
        }

        val entrepreneurLayout: LinearLayout = findViewById(R.id.entrepreneurLayout)
        entrepreneurLayout.setOnClickListener {
            val intent = Intent(this, EntrepreneurActivity::class.java)
            myContract.launch(intent)
        }

        val governmentLayout: LinearLayout = findViewById(R.id.governmentLayout)
        governmentLayout.setOnClickListener {
            val intent = Intent(this, GovermentActivity::class.java)
            myContract.launch(intent)
        }

        val criminalLayout: LinearLayout = findViewById(R.id.criminalLayout)
        criminalLayout.setOnClickListener {
            val intent = Intent(this, CrimeActivity::class.java)
            myContract.launch(intent)
        }
    }
}

