package io.audioshinigami.superd.downloads

import androidx.recyclerview.widget.RecyclerView
import io.audioshinigami.superd.BR
import io.audioshinigami.superd.databinding.ItemBinding
import io.audioshinigami.superd.data.FileInfo

class FileInfoViewHolder(private val binding: ItemBinding )
    : RecyclerView.ViewHolder(binding.root) {

    fun bind( downloadItemActions: DownloadItemActions ,any: FileInfo){
        binding.apply {
            setVariable(BR.fileInfo, any)
            itemAction = downloadItemActions
            executePendingBindings()

        }

    }

    fun clear(){
        binding.unbind()
    }

}