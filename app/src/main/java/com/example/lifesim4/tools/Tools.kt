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
import com.example.lifesim4.R
import com.example.lifesim4.models.Asset
import com.example.lifesim4.models.Character
import com.example.lifesim4.models.GameEngine
import kotlin.math.absoluteValue

object Tools {
    fun <T> addCardToView(
        context: Context,
        cardObject: T,
        placement: LinearLayout,
        caption: String,
        icon: Int,
        nextActivity: Class<*>?,
        contract: ActivityResultLauncher<Intent>,
    ) {
        val inflater = LayoutInflater.from(context)
        val personCard = inflater.inflate(R.layout.card_basic, placement, false)

        val nameTextView: TextView = personCard.findViewById(R.id.name)
        val captionTextView: TextView = personCard.findViewById(R.id.caption)
        val image: ImageView = personCard.findViewById(R.id.image)

        var asset = cardObject as Asset

        if (cardObject is Character || cardObject is Asset) {
            if (cardObject is Character) {
                val character = cardObject as Character
                nameTextView.text = character.name
            } else if (cardObject is Asset) {
                asset = cardObject
                nameTextView.text = asset.name
            }

            personCard.setOnClickListener {
                if (nextActivity != null) {
                    val intent = Intent(context, nextActivity)
                    intent.putExtra("ObjectName", nameTextView.text)
                    contract.launch(intent)
                } else {
                    //for assets to buy
                    showPopupDialog(context, "Would you like to buy for ${formatMoney(asset.value.toLong())}", asset)
                    //contract.launch(Intent().apply { putExtra("result", Activity.RESULT_OK) })
                }
            }
        }
        captionTextView.text = caption
        image.setImageResource(icon)

        placement.addView(personCard)
    }

    fun <T> showPopupDialog(context: Context, message: String, obj: T) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_layout)
        val gameEngine = GameEngine.getInstance()

        val dialogMessage: TextView = dialog.findViewById(R.id.dialog_message)
        val dialogButton: TextView = dialog.findViewById(R.id.dialog_button)

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialogMessage.text = message

        if (obj is Asset && obj.boughtFor == -1L){
            dialogButton.text = "Buy"
            dialogButton.setOnClickListener {
                gameEngine.buyAsset(obj)
                dialog.dismiss()
            }
        } else {
            dialogButton.setOnClickListener {
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

}
