package com.example.lifesim4.tools

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.Window
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.lifesim4.R
import com.example.lifesim4.models.Person
import java.util.Objects

object Tools {
    fun addCardToView(
        context: Context,
        cardObject: Objects,
        placement: LinearLayout,
        name: String?,
        caption: String,
        icon: Int,
        nextActivity: Class<*>,
        contract: ActivityResultLauncher<Intent>
    ) {
        val inflater = LayoutInflater.from(context)
        val personCard = inflater.inflate(R.layout.card_basic, placement, false)

        val nameTextView: TextView = personCard.findViewById(R.id.name)
        val captionTextView: TextView = personCard.findViewById(R.id.caption)
        val image: ImageView = personCard.findViewById(R.id.image)

        nameTextView.text = name
        captionTextView.text = caption
        image.setImageResource(icon)

        personCard.setOnClickListener {
            val intent = Intent(context, nextActivity)
            contract.launch(intent)
        }

        placement.addView(personCard)
    }

    fun showPopupDialog(context: Context, message: String) {
        val dialog = Dialog(context)
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
