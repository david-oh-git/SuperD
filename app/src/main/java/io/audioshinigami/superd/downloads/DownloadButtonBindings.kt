/*
 * MIT License
 *
 * Copyright (c) 2020  David Osemwota
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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