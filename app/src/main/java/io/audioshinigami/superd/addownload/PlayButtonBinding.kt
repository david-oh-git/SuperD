package io.audioshinigami.superd.addownload

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import io.audioshinigami.superd.R

object PlayButtonBinding {

    @BindingAdapter( "icon" )
    @JvmStatic fun setImageViewResource(imageView: ImageView, active: Boolean ){
        with(imageView){
            if(active)
                setImageResource(R.drawable.ic_pause)
            else
                setImageResource(R.drawable.ic_play)
        }
    }
}