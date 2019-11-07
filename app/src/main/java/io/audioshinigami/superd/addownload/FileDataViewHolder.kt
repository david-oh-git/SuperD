package io.audioshinigami.superd.addownload

import androidx.recyclerview.widget.RecyclerView
import io.audioshinigami.superd.BR
import io.audioshinigami.superd.databinding.DownloadItem2Binding

class FileDataViewHolder( private val binding: DownloadItem2Binding )
    : RecyclerView.ViewHolder(binding.root) {

    fun bind( any: Any){
        binding.apply {
            setVariable(BR.fileData, any)
            executePendingBindings()
        }
    }

    fun clear(){
        binding.apply {
            setVariable(BR.fileData, null )
            executePendingBindings()
        }
    }
}