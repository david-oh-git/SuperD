package io.audioshinigami.superd.utility.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdaptor: RecyclerView.Adapter<AppViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        val binding = DataBindingUtil.inflate<ViewDataBinding>(layoutInflater, viewType ,
            parent, false)

        return AppViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
        val anyObject = getPositionDataObject(position)

        holder.bind(anyObject)
    }

    override fun getItemViewType(position: Int) = getLayoutIdForPosition(position)

    abstract fun getPositionDataObject(position: Int ): Any

    /* retrieve layout ID for position , so it can use different layouts*/
    abstract fun getLayoutIdForPosition( position: Int ): Int
}