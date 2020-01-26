package io.audioshinigami.superd.downloads

import android.widget.ImageButton
import androidx.databinding.BindingAdapter
import io.audioshinigami.superd.App
import io.audioshinigami.superd.R
import io.audioshinigami.superd.zdata.FileInfo
import io.audioshinigami.superd.zdata.source.State.*

/*
* contains bindAdaptor for ImageButton
* */

object ImageButtonBindings {

    @BindingAdapter("app:itemData")
    @JvmStatic fun setItemData(button: ImageButton, _data: FileInfo ){
        with(button){

            val state = ((context.applicationContext) as App ).activeDownloads[_data.request_id]
            when{
                _data.progressValue == 100 -> {
                    setImageResource(R.drawable.ic_done)
                    isEnabled = false
                }

                state == COMPLETE -> {
                    setImageResource(R.drawable.ic_done)
                    isEnabled = false
                }

                state == DOWNLOADING -> setImageResource(R.drawable.ic_pause)

                state == PAUSED  -> setImageResource(R.drawable.ic_download )

                state == ERROR -> setImageResource(R.drawable.ic_error)

                _data.progressValue < 0 -> setImageResource(R.drawable.ic_error)
//
                state == null -> setImageResource(R.drawable.ic_download )

//                else -> setImageResource(R.drawable.ic_download)
            }
        }
    }
}