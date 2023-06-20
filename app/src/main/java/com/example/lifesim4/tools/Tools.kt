package com.example.lifesim4.tools

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.Window
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.lifesim4.R

object Tools {
    fun addPersonToView(
        context: Context,
        placement: LinearLayout,
        name: String?,
        caption: String,
        icon: Int
    ) {
        // Get LayoutInflater instance from the context
        val inflater = LayoutInflater.from(context)

        // Create an instance of the card_basic layout
        val personCard = inflater.inflate(R.layout.card_basic, placement, false)

        // Find the views inside the personCard layout and set the person's details
        val nameTextView: TextView = personCard.findViewById(R.id.name)
        val captionTextView: TextView = personCard.findViewById(R.id.caption)
        val image: ImageView = personCard.findViewById(R.id.image)

        nameTextView.text = name
        captionTextView.text = caption
        image.setImageResource(icon)

        personCard.setOnClickListener {
            showPopupDialog(context, "Family")
        }

        // Add the personCard to the placement LinearLayout
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
