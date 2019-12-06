package io.audioshinigami.superd.addownload

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import io.audioshinigami.superd.R.layout.download_item
import io.audioshinigami.superd.data.source.db.entity.FileData
import io.audioshinigami.superd.databinding.DownloadItemBinding

class FileDataAdaptor(
    private val downloadItemActions: DownloadItemActions
):
    PagedListAdapter<FileData, FileDataViewHolder>(FileDataDiffCallback()) {

//    var binding: DownloadItem2Binding? = null

    override fun onBindViewHolder(holder: FileDataViewHolder, position: Int) {
        val fileData = getItem(position)

        fileData?.apply {
            holder.bind( downloadItemActions ,this)
        } ?: holder.clear()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileDataViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        val binding = DownloadItemBinding.inflate(layoutInflater, parent, false)

        return FileDataViewHolder(binding)
    }

    override fun getItemViewType(position: Int) = download_item

    private class FileDataDiffCallback: DiffUtil.ItemCallback<FileData>() {

        override fun areItemsTheSame(oldItem: FileData, newItem: FileData): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: FileData, newItem: FileData): Boolean {
            return oldItem == newItem
        }
    }
}