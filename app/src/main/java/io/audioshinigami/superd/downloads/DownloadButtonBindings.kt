package io.audioshinigami.superd.downloads

import androidx.appcompat.widget.AppCompatImageButton
import androidx.databinding.BindingAdapter
import io.audioshinigami.superd.App
import io.audioshinigami.superd.R
import io.audioshinigami.superd.data.source.State

object DownloadButtonBindings {

    @BindingAdapter("app:progress","app:request_id")
    @JvmStatic fun setData(button: AppCompatImageButton, progress: Int, requestId: Int ){
        with(button){

            val state = ((context.applicationContext) as App).activeDownloads[requestId]
            when{
                progress == 100 || state == State.COMPLETE  -> {
                    setImageResource(R.drawable.ic_done)
                    isEnabled = false
                }

                state == State.DOWNLOADING -> setImageResource(R.drawable.ic_pause)

                state == State.PAUSED || state == null -> setImageResource(R.drawable.ic_play )

                progress <= 0 || state == State.ERROR -> setImageResource(R.drawable.ic_error)

//                else -> setImageResource(R.drawable.ic_download)
            }
        }
    }
}