package com.example.lifesim4.relations

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import com.example.lifesim4.R
import com.example.lifesim4.RelationsActivity
import com.example.lifesim4.models.GameEngine
import com.example.lifesim4.models.Person
import com.example.lifesim4.tools.Tools

class FamilyActivity : AppCompatActivity() {

    private lateinit var gameEngine: GameEngine
    private lateinit var player: Person
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.relation_family)
        gameEngine = GameEngine.getInstance()
        player = gameEngine.getPlayer()

        val myContract = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                setResult(Activity.RESULT_OK)
                finish()
            }
        }

        val parentsContainer: LinearLayout = findViewById(R.id.parents)
        val siblingsContainer: LinearLayout = findViewById(R.id.siblings)
        val childrenContainer: LinearLayout = findViewById(R.id.children)

        addPersonToView(parentsContainer, player.father?.name, "Father", R.drawable.male)
        addPersonToView(parentsContainer, player.mother?.name, "Mother", R.drawable.female)

        player.brothers.forEach{ person ->
            Tools.addCardToView(this, person,  siblingsContainer, person.name , "Health ${person.health}%", R.drawable.male, PersonActivity::class.java, myContract)
        }
        player.sisters.forEach{ person ->
            Tools.addCardToView(this, person, siblingsContainer, person.name , "Health ${person.health}%", R.drawable.female, PersonActivity::class.java, myContract)
        }
        player.children.forEach{ person ->
            Tools.addCardToView(this, person, childrenContainer, person.name , "Health ${person.health}%", R.drawable.baby, PersonActivity::class.java, myContract)
        }
    }

    private fun addPersonToView(placement: LinearLayout, name: String?, caption: String, icon: Int){
        // Create an instance of the card_basic layout
        val personCard = layoutInflater.inflate(R.layout.card_basic, placement, false)

        // Find the views inside the fatherCard layout and set the father's details
        val nameTextView: TextView = personCard.findViewById(R.id.name)
        val captionTextView: TextView = personCard.findViewById(R.id.caption)
        val image: ImageView = personCard.findViewById(R.id.image)

        nameTextView.text = name
        captionTextView.text = caption
        image.setImageResource(icon)

        personCard.setOnClickListener {
            showPopupDialog("Family")
        }

        placement.addView(personCard)
        //return personCard
    }

    fun showPopupDialog(message: String) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_layout)

        val dialogMessage: TextView = dialog.findViewById(R.id.dialog_message)
        val dialogButton: TextView = dialog.findViewById(R.id.dialog_button)

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialogMessage.text = message

        dialogButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)

        dialog.show()
    }
}