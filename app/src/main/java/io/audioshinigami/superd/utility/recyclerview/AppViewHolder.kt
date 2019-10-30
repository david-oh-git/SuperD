package io.audioshinigami.superd.utility.recyclerview

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import io.audioshinigami.superd.BR

/*
* reusable recyclerView viewHolder
*/

class AppViewHolder( private val binding: ViewDataBinding )
    : RecyclerView.ViewHolder(binding.root){

    fun bind(any: Any){

        binding.apply {
            setVariable(BR.fileData, any)
            executePendingBindings()
        }
    }
}