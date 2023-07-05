package com.example.lifesim4.tools

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import com.example.lifesim4.R
import com.example.lifesim4.models.Asset
import com.example.lifesim4.models.Character
import com.example.lifesim4.models.GameEngine
import com.example.lifesim4.models.Job
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.absoluteValue

object Tools {

    data class CardWithAsset<T>(val personCard: View, val obj: T)
    fun <T> addCardsToView(
        context: Context,
        cardObjects: List<T>,
        placement: LinearLayout,
        caption: String,
        icon: Int,
        nextActivity: Class<*>?,
        contract: ActivityResultLauncher<Intent>,
    ): MutableList<CardWithAsset<T>> {
        val cards = mutableListOf<CardWithAsset<T>>()

        cardObjects.forEach { cardObject ->
            val card = addCardToView(context, cardObject, placement, caption, icon, nextActivity, contract)
            cards.add(card)
        }

        return cards
    }
    fun <T> addCardToView(
        context: Context,
        cardObject: T,
        placement: LinearLayout,
        caption: String,
        icon: Int,
        nextActivity: Class<*>?,
        contract: ActivityResultLauncher<Intent>,
    ): CardWithAsset<T> {
        val inflater = LayoutInflater.from(context)
        val personCard = inflater.inflate(R.layout.card_basic, placement, false)

        val nameTextView: TextView = personCard.findViewById(R.id.name)
        val captionTextView: TextView = personCard.findViewById(R.id.caption)
        val costTextView: TextView = personCard.findViewById(R.id.value)
        val image: ImageView = personCard.findViewById(R.id.image)

        image.setImageResource(icon)

        if (cardObject is Character || cardObject is Asset) {
            if (cardObject is Character) {
                val character = cardObject as Character
                nameTextView.text = character.name
                costTextView.visibility = View.GONE
            } else if (cardObject is Asset) {
                val asset = cardObject
                nameTextView.text = asset.name
                costTextView.text = formatMoney(asset.value.toLong())
                image.setImageResource(asset.icon)
            }

            personCard.setOnClickListener {
                if (nextActivity != null) {
                    val intent = Intent(context, nextActivity)
                    if (cardObject is Asset){
                        intent.putExtra("ObjectID", (cardObject.id).toString())
                    }
                    intent.putExtra("ObjectName", nameTextView.text)
                    contract.launch(intent)
                }
            }
        } else if (cardObject is Job){
            nameTextView.text = cardObject.name
            var formattedSalary = ""
            if (cardObject.salary < 10000000){
                val decimalFormat = DecimalFormat("#,###")
                formattedSalary = "$" + decimalFormat.format(cardObject.salary)
            } else {
                formattedSalary = formatMoney(cardObject.salary.toLong())
            }

            costTextView.text = "Salary\n" +  formattedSalary                           //NumberFormat.getCurrencyInstance(Locale.US).format(cardObject.salary.toLong())
            image.setImageResource(cardObject.icon)
        }
        captionTextView.text = caption
        placement.addView(personCard)

        return CardWithAsset(personCard, cardObject)
    }

    fun <T> showPopupDialog(context: Context, message: String, button1Text: String, button2Text:String, obj: T, resultCallback: ((Int, Int) -> Unit)?) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_layout)

        val dialogMessage: TextView = dialog.findViewById(R.id.dialog_message)
        val dialogButton: TextView = dialog.findViewById(R.id.dialog_button)
        val dialogButton2: TextView = dialog.findViewById(R.id.dialog_button2)

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialogMessage.text = message
        dialogButton.text = button1Text
        dialogButton2.text = button2Text

        if (button2Text == ""){
            dialogButton2.visibility = View.GONE
            dialogButton.setOnClickListener {
                dialog.dismiss()
            }
        }

        if (obj is Asset){
            dialogButton.setOnClickListener {
                resultCallback?.invoke(Activity.RESULT_OK, 1) // Button 1 (Rent) pressed
                dialog.dismiss()
            }
            dialogButton2.setOnClickListener {
                resultCallback?.invoke(Activity.RESULT_OK, 2) // Button 2 (Buy) pressed
                dialog.dismiss()
            }
        }

        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        dialog.show()
    }

    fun formatMoney(amount: Long): String {
        val suffixes = listOf("", "K", "M", "B", "T", "Q", "Qu", "S")
        val suffixIndex = (Math.max(0, Math.floor(Math.log10(amount.toDouble().absoluteValue) / 3).toInt())).coerceAtMost(suffixes.size - 1)
        val shortValue = amount.toDouble().absoluteValue / Math.pow(10.0, (suffixIndex * 3).toDouble())
        val formattedValue = "%.2f".format(shortValue)
        val sign = if (amount < 0) "-" else ""
        return "$$sign$formattedValue${suffixes[suffixIndex]}"
    }

    fun formatNetWorthMoney(amount: Long): String {
        val suffixes = listOf("", "K", "M", "B", "T", "Q", "Qu", "S")
        val suffixIndex = (Math.max(0, Math.floor(Math.log10(amount.toDouble().absoluteValue) / 3).toInt())).coerceAtMost(suffixes.size - 1)
        val shortValue = amount.toDouble().absoluteValue / Math.pow(10.0, (suffixIndex * 3).toDouble())
        val formattedValue = "%.2f".format(shortValue)
        val sign = if (amount < 0) "-" else ""
        return "$$formattedValue${suffixes[suffixIndex]}"
    }

}
