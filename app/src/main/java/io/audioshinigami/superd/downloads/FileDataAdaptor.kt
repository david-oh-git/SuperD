package io.audioshinigami.superd.downloads

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import io.audioshinigami.superd.R.layout.download_item
import io.audioshinigami.superd.databinding.DownloadItemBinding
import io.audioshinigami.superd.zdata.FileInfo

class FileDataAdaptor(
    private val downloadItemActions: DownloadItemActions
):
    PagedListAdapter<FileInfo, FileDataViewHolder>(FileDataDiffCallback()) {

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

    private class FileDataDiffCallback: DiffUtil.ItemCallback<FileInfo>() {

        override fun areItemsTheSame(oldItem: FileInfo, newItem: FileInfo): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: FileInfo, newItem: FileInfo): Boolean {
            return oldItem == newItem
        }
    }
}