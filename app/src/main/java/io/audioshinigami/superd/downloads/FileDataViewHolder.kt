package io.audioshinigami.superd.downloads

import androidx.recyclerview.widget.RecyclerView
import io.audioshinigami.superd.BR
import io.audioshinigami.superd.databinding.DownloadItemBinding
import io.audioshinigami.superd.zdata.FileInfo

class FileDataViewHolder( private val binding: DownloadItemBinding )
    : RecyclerView.ViewHolder(binding.root) {

    fun bind( downloadItemActions: DownloadItemActions ,any: FileInfo ){
        binding.apply {
            setVariable(BR.fileData, any)
            itemAction = downloadItemActions
            executePendingBindings()

        }

    }

    fun clear(){
        binding.unbind()
    }

}