package com.example.powerhouseevaluation.bindingadapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.example.powerhouseevaluation.R
import com.squareup.picasso.Picasso

class RowBinding {
    companion object{
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