package io.audioshinigami.superd.downloads

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import io.audioshinigami.superd.R.layout.download_item
import io.audioshinigami.superd.databinding.ItemBinding
import io.audioshinigami.superd.zdata.FileInfo
import kotlin.math.abs

class FileInfoAdaptor(
    private val downloadItemActions: DownloadItemActions
):
    PagedListAdapter<FileInfo, FileInfoViewHolder>(FileDataDiffCallback()) {

//    var binding: DownloadItem2Binding? = null

    override fun onBindViewHolder(holder: FileInfoViewHolder, position: Int) {
        val fileInfo = getItem(position)

        fileInfo?.apply {
            holder.bind( downloadItemActions ,this.copy( progressValue = abs(progressValue) ) )
        } ?: holder.clear()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileInfoViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        val binding = ItemBinding.inflate(layoutInflater, parent, false)

        return FileInfoViewHolder(binding)
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