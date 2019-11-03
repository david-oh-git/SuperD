package io.audioshinigami.superd.addownload

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import io.audioshinigami.superd.data.source.db.entity.FileData
import io.audioshinigami.superd.databinding.DownloadItem2Binding
import io.audioshinigami.superd.utility.recyclerview.AppViewHolder
import io.audioshinigami.superd.utility.recyclerview.BaseAdaptor

class DownloadAdaptor(private val layoutID: Int ):
 BaseAdaptor() {

    private var data: List<FileData>? = null

    override var itemClickListener: View.OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

//        val binding = DataBindingUtil.inflate<ViewDataBinding>(layoutInflater, viewType ,
//            parent, false)

        val binding = DownloadItem2Binding.inflate(layoutInflater, parent, false)


        return AppViewHolder(binding)
    }

    override fun getLayoutIdForPosition(position: Int) = layoutID

    override fun getPositionDataObject(position: Int) = data?.get(position) as FileData

    override fun getItemCount() = data?.size ?: 0

    fun setData( currentData : List<FileData>){
        data = currentData

        notifyDataSetChanged()
    }

    fun updateProgressValue( newProgressValue: Int, url: String ){

    }

    private class FileDataDiffCallback(
        private val oldList: List<FileData>,
        private val newList: List<FileData> ): DiffUtil.Callback() {

        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].url == newList[newItemPosition].url
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]

            return ( oldItem.url == newItem.url ) &&
                    ( oldItem.progressValue == newItem.progressValue ) &&
                    ( oldItem.isActive == newItem.isActive )
        }

    }

}