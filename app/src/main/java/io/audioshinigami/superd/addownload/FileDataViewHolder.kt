package io.audioshinigami.superd.addownload

import androidx.recyclerview.widget.RecyclerView
import io.audioshinigami.superd.BR
import io.audioshinigami.superd.data.source.db.entity.FileData
import io.audioshinigami.superd.databinding.DownloadItem2Binding

class FileDataViewHolder( private val binding: DownloadItem2Binding )
    : RecyclerView.ViewHolder(binding.root) {

    fun bind( any: FileData ){
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

    /* sets progress value, function is passed to viewModel for realtime updates*/
    fun setProgressValue(url: String, value: Int){
        if( binding.fileData.url == url ) binding.idDwloadprogressbar.progress = value

    }
}