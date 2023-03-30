package com.example.powerhouseevaluation.bindingadapter

import android.util.Log
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import com.example.powerhouseevaluation.R
import com.example.powerhouseevaluation.models.ForecastdayX
import com.example.powerhouseevaluation.ui.fragments.BonusCitiesFragmentDirections
import com.squareup.picasso.Picasso

class RowBinding {
    companion object{
        @BindingAdapter("onWeatherClickListener")
        @JvmStatic
        fun onWeatherClickListener(forecastrowlayout: ConstraintLayout, result: ForecastdayX) {
            Log.d("onRecipeClickListener", "CALLED")
            forecastrowlayout.setOnClickListener {
                try {
                    val action = BonusCitiesFragmentDirections.actionBonusCitiesFragmentToDisplayBonusFragment(result)
                    forecastrowlayout.findNavController().navigate(action)
                } catch (e: Exception) {
                    Log.d("onRecipeClickListener", e.toString())
                }
            }
        }

        @BindingAdapter("loadImageFromUrl")
        @JvmStatic
        fun loadImageFromUrl(imageView: ImageView, imageUrl: String?) {
            if (imageUrl == null) {
                imageView.setImageResource(R.drawable.common_google_signin_btn_icon_dark)
            } else {
                Picasso.get()
                    .load(imageUrl)
                    .resize(500, 500) // resize the image to 500x500 pixels
                    .centerCrop() // crop the image to fit the ImageView
                    .into(imageView)
            }
        }
    }
}