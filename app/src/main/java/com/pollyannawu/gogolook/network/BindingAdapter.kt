package com.pollyannawu.gogolook.network


import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pollyannawu.gogolook.R



@BindingAdapter("imageAvatar")
fun imageAvatar(imgView: ImageView, imgUrl: String?) {
    try {
        imgUrl?.let {
            val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()

            Glide.with(imgView.context)
                .load(imgUri)
                .apply(
                    RequestOptions()
                        .circleCrop()
                        .placeholder(R.drawable.gogolook)
                )
                .into(imgView)

        }
    } catch (e: Exception) {
        Log.i("fail_load_image", "${e}")
    }

}

@BindingAdapter("imageUrl")
fun imageUrl(imgView: ImageView, imgUrl: String?) {
    try {
        imgUrl?.let {
            val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()

            Glide.with(imgView.context)
                .load(imgUri)
                .apply(
                    RequestOptions()
                        .placeholder(R.drawable.gogolook)
                )
                .into(imgView)

        }
    } catch (e: Exception) {
        Log.i("fail_load_image", "${e}")
    }

}

@BindingAdapter("numberText")
fun numberText(textView: TextView, number: Int){
    textView.setTextColor(textView.context.getColor(R.color.primary))
    if (number > 999){
        textView.text = textView.context.getString(R.string.over_999)
    }else{
        textView.text = number.toString()
    }

}



