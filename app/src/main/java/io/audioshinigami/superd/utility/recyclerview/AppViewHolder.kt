package io.audioshinigami.superd.utility.recyclerview

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import io.audioshinigami.superd.BR
import io.audioshinigami.superd.databinding.DownloadItem2Binding

/*
* reusable recyclerView viewHolder
*/

class AppViewHolder( private val binding: DownloadItem2Binding )
    : RecyclerView.ViewHolder(binding.root){

    fun bind( _clickListener: View.OnClickListener? , any: Any){

        binding.apply {
            setVariable(BR.fileData, any)
            executePendingBindings()
            clickListener = _clickListener

        }

    }

    fun clear() {
        binding.apply {
            setVariable(BR.fileData, null)
            executePendingBindings()
        }
    }
}