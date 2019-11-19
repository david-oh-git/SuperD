package io.audioshinigami.superd.addownload

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import io.audioshinigami.superd.SharedViewModel
import io.audioshinigami.superd.data.source.db.entity.FileData
import io.audioshinigami.superd.databinding.DownloadItem2Binding
import io.audioshinigami.superd.utility.recyclerview.AppViewHolder
import io.audioshinigami.superd.utility.recyclerview.BaseAdaptor

class DownloadAdaptor(
    private val layoutID: Int,
    private val viewModel: SharedViewModel
): BaseAdaptor() {

    private val data: ArrayList<FileData> = ArrayList()

    override var itemClickListener: View.OnClickListener? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

//        val binding = DataBindingUtil.inflate<ViewDataBinding>(layoutInflater, viewType ,
//            parent, false)

        val binding = DownloadItem2Binding.inflate(layoutInflater, parent, false)

        return AppViewHolder(binding!!)
    }

    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
        val anyObject = getPositionDataObject(position)

        val action = ItemActions( ::downloadButtonAction, ::overFlowButtonAction )

        holder.bind( action, anyObject)
    }

    override fun getLayoutIdForPosition(position: Int) = layoutID

    override fun getPositionDataObject(position: Int) = data[position]

    override fun getItemCount() = data.size

    fun setData( currentData : List<FileData>){

        val  diffcallback = FileDataDiffCallback(data, currentData)
        val  diffResult = DiffUtil.calculateDiff(diffcallback)

        data.clear()
        data.addAll(currentData)

        diffResult.dispatchUpdatesTo(this)
    }

    fun updateProgressValue( newProgressValue: Int, url: String ){

    }

    private fun downloadButtonAction( id: Int , url: String ){
        viewModel.downloadActionClick( id, url)
    }

    private fun overFlowButtonAction( fileData: FileData){
        // TODo inflate popUp Menu
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