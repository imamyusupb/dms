package seino.indomobil.dmsmobile.presentation.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import seino.indomobil.dmsmobile.R

@BindingAdapter("android:setImageResource")
fun ImageView.setImageResource(drawable: Int) {
    try {
        this.setImageResource(drawable)
    } catch (e: Exception) {
        e.toString()
    }
}

@BindingAdapter("loadImage")
fun loadImage(imagaView:ImageView,urlString:String?){
    urlString?.let {
        Glide.with(imagaView)
            .load(urlString)
            .placeholder(R.drawable.img_placeholder)
            .error(R.drawable.img_placeholder)
            .into(imagaView)
    }
}
