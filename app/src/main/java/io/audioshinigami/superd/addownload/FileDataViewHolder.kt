package io.audioshinigami.superd.addownload

import androidx.recyclerview.widget.RecyclerView
import io.audioshinigami.superd.BR
import io.audioshinigami.superd.R
import io.audioshinigami.superd.data.source.db.entity.FileData
import io.audioshinigami.superd.databinding.DownloadItemBinding

class FileDataViewHolder( private val binding: DownloadItemBinding )
    : RecyclerView.ViewHolder(binding.root) {

    fun bind( downloadItemActions: DownloadItemActions ,any: FileData ){
        binding.apply {
            setVariable(BR.fileData, any)
            itemAction = downloadItemActions

            /* if progress is completed disable download button & add done icon */
            if( any.progressValue == 100 ) {
                binding.idPlayPause.isEnabled = false
                binding.idPlayPause.setImageResource( R.drawable.ic_done )
            }

            executePendingBindings()
        }

    }

    fun clear(){
        binding.unbind()
    }

}