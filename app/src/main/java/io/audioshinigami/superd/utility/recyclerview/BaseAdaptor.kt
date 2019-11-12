package io.audioshinigami.superd.utility.recyclerview

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdaptor: RecyclerView.Adapter<AppViewHolder>() {

    abstract var itemClickListener: View.OnClickListener?

    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
        val anyObject = getPositionDataObject(position)

        holder.bind(itemClickListener, anyObject)
    }

    override fun getItemViewType(position: Int) = getLayoutIdForPosition(position)

    abstract fun getPositionDataObject(position: Int ): Any

    /* retrieve layout ID for position , so it can use different layouts*/
    abstract fun getLayoutIdForPosition( position: Int ): Int
}