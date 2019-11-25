package io.audioshinigami.superd.addownload

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import io.audioshinigami.superd.R.layout.download_item_2
import io.audioshinigami.superd.SharedViewModel
import io.audioshinigami.superd.data.source.db.entity.FileData
import io.audioshinigami.superd.databinding.DownloadItem2Binding
import timber.log.Timber

class FIleDataAdaptor(
    private val viewModel: SharedViewModel
):
    PagedListAdapter<FileData, FileDataViewHolder>(FileDataDiffCallback()) {

//    var binding: DownloadItem2Binding? = null

    override fun onBindViewHolder(holder: FileDataViewHolder, position: Int) {
        val fileData = getItem(position)

        val action = ItemActions( ::downloadButtonAction, ::overFlowButtonAction )

        fileData?.apply {
            holder.bind( action,this)
        } ?: holder.clear()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileDataViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        val binding = DownloadItem2Binding.inflate(layoutInflater, parent, false)

        return FileDataViewHolder(binding!!)
    }

    override fun getItemViewType(position: Int) = download_item_2

    private fun downloadButtonAction( id: Int , url: String ){
        Timber.d( "download action called. id: $id & url : $url")
        viewModel.downloadAction( id, url)
    }

    private fun overFlowButtonAction( fileData: FileData){
        // TODO inflate popUp Menu
    }

    private class FileDataDiffCallback: DiffUtil.ItemCallback<FileData>() {

        override fun areItemsTheSame(oldItem: FileData, newItem: FileData): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: FileData, newItem: FileData): Boolean {
            return oldItem == newItem
        }
    }
}