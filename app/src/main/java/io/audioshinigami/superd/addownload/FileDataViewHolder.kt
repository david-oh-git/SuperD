package io.audioshinigami.superd.addownload

import androidx.recyclerview.widget.RecyclerView
import io.audioshinigami.superd.BR
import io.audioshinigami.superd.data.source.db.entity.FileData
import io.audioshinigami.superd.databinding.DownloadItem2Binding

class FileDataViewHolder( private val binding: DownloadItem2Binding )
    : RecyclerView.ViewHolder(binding.root) {

    fun bind( downloadItemActions: DownloadItemActions ,any: FileData ){
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