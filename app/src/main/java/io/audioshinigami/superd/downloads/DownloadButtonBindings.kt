package io.audioshinigami.superd.downloads

import androidx.appcompat.widget.AppCompatImageButton
import androidx.databinding.BindingAdapter
import io.audioshinigami.superd.App
import io.audioshinigami.superd.R
import io.audioshinigami.superd.zdata.source.State

object DownloadButtonBindings {

    @BindingAdapter("app:progress","app:request_id")
    @JvmStatic fun setData(button: AppCompatImageButton, progress: Int, requestId: Int ){
        with(button){

            val state = ((context.applicationContext) as App).activeDownloads[requestId]
            when{
                progress == 100 -> {
                    setImageResource(R.drawable.ic_done)
                    isEnabled = false
                }

                state == State.COMPLETE -> {
                    setImageResource(R.drawable.ic_done)
                    isEnabled = false
                }

                state == State.DOWNLOADING -> setImageResource(R.drawable.ic_pause)

                state == State.PAUSED -> setImageResource(R.drawable.ic_play )

                state == State.ERROR -> setImageResource(R.drawable.ic_error)

                progress < 0 -> setImageResource(R.drawable.ic_error)
//
                state == null -> setImageResource(R.drawable.ic_play )

//                else -> setImageResource(R.drawable.ic_download)
            }
        }
    }
}