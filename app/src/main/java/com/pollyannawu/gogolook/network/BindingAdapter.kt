package com.pollyannawu.gogolook.network

import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pollyannawu.gogolook.R

@BindingAdapter("imageUrl")
fun bindingImage(imageView: ImageView, imageUrl: String?){
    imageUrl?.let {imageUrl ->
        val imageUri = imageUrl.toUri().buildUpon().build()

        Glide.with(imageView.context)
            .load(imageUri)
            .apply  (
                RequestOptions()
                    .placeholder(R.drawable.gogolook)
                    .error(R.drawable.gogolook)
            )
    }
}