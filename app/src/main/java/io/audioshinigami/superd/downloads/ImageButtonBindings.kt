package io.audioshinigami.superd.downloads

import android.widget.ImageButton
import androidx.databinding.BindingAdapter
import io.audioshinigami.superd.App
import io.audioshinigami.superd.R
import io.audioshinigami.superd.zdata.FileInfo

/*
* contains bindAdaptor for ImageButton
* */

object ImageButtonBindings {

    @BindingAdapter("app:itemData")
    @JvmStatic fun setItemData(button: ImageButton, _data: FileInfo ){
        with(button){

            val activeDownloads = ((context.applicationContext) as App ).activeDownloads
            when{
                _data.progressValue == 100 -> {
                    setImageResource(R.drawable.ic_done)
                    isEnabled = false
                }

                activeDownloads[_data.request_id] == true -> setImageResource(R.drawable.ic_pause)

                activeDownloads[_data.request_id] == false -> setImageResource(R.drawable.ic_download )

                _data.progressValue < 0 -> setImageResource(R.drawable.ic_error)
//

                activeDownloads[_data.request_id] == null -> setImageResource(R.drawable.ic_download )

//                else -> setImageResource(R.drawable.ic_download)
            }
        }
    }
}