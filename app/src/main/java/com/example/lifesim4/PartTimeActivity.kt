package com.example.lifesim4

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout

class PartTimeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_part_time)

        val partTimejob : LinearLayout = findViewById(R.id.partTimeJobLayout)

        partTimejob.setOnClickListener {

            //gameEngine.simulate()
            val resultIntent = Intent()
            resultIntent.putExtra("Part Time", "New Part time Job, You are now a swimming instructor")
            setResult(Activity.RESULT_OK, resultIntent)
            finish()

        }
    }
}