package io.audioshinigami.superd.downloads

import android.widget.ImageButton
import androidx.databinding.BindingAdapter
import io.audioshinigami.superd.App
import io.audioshinigami.superd.R
import io.audioshinigami.superd.data.source.db.entity.FileData

/*
* contains bindAdaptor for ImageButton
* */

object ImageButtonBindings {

    @BindingAdapter("app:itemData")
    @JvmStatic fun setItemData(button: ImageButton, _data: FileData ){
        with(button){

            val activeDownloads = ((this.context.applicationContext) as App ).isActive
            when{
                _data.progressValue == 100 -> {
                    setImageResource(R.drawable.ic_done)
                    isEnabled = false
                }

                activeDownloads[_data.url] == true -> setImageResource(R.drawable.ic_pause)

                activeDownloads[_data.url] == false -> setImageResource(R.drawable.ic_download )

                _data.progressValue < 0 -> setImageResource(R.drawable.ic_error)
//

                activeDownloads[_data.url] == null -> setImageResource(R.drawable.ic_download )

//                else -> setImageResource(R.drawable.ic_download)
            }
        }
    }
}