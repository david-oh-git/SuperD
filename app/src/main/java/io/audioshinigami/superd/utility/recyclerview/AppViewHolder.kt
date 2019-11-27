package io.audioshinigami.superd.utility.recyclerview

import androidx.recyclerview.widget.RecyclerView
import io.audioshinigami.superd.BR
import io.audioshinigami.superd.databinding.DownloadItem2Binding

/*
* reusable recyclerView viewHolder
*/

class AppViewHolder( private val binding: DownloadItem2Binding )
    : RecyclerView.ViewHolder(binding.root){

    fun bind( any: Any){

        binding.apply {
            setVariable(BR.fileData, any)
            executePendingBindings()
        }

    }

    fun unbind() {
        binding.unbind()
    }
}