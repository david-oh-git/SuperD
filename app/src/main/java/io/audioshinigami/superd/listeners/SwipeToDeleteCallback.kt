package io.audioshinigami.superd.listeners

import android.graphics.*
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import io.audioshinigami.superd.viewholders.ItemViewHolder

class SwipeToDeleteCallback(dragDirs: Int , swipeDirs: Int, var listener: SwipeToDeleteListener) :
    ItemTouchHelper.SimpleCallback(dragDirs, swipeDirs) {

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int
            = makeMovementFlags(0, ItemTouchHelper.LEFT) /*end getMovementFlags*/

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean = true

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        if( viewHolder != null && viewHolder is ItemViewHolder){
            val foregroundView: View = viewHolder.foregroundView
            ItemTouchHelper.Callback.getDefaultUIUtil().onSelected(foregroundView)
        } /*end IF*/

    }

    override fun onChildDrawOver(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder?,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        if(viewHolder is ItemViewHolder){
            val foregroundView: View = viewHolder.foregroundView
            ItemTouchHelper.Callback.getDefaultUIUtil()
                .onDrawOver(c, recyclerView, foregroundView, dX, dY, actionState, isCurrentlyActive)
        } /*end IF*/

    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        if( viewHolder is ItemViewHolder ){
            val foreground: View = viewHolder.foregroundView
            ItemTouchHelper.Callback.getDefaultUIUtil().clearView(foreground)
        } /* end IF*/
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        if ( viewHolder is ItemViewHolder ){
            val foreground: View = viewHolder.foregroundView
            ItemTouchHelper.Callback.getDefaultUIUtil()
                .onDraw(c, recyclerView, foreground, dX, dY, actionState, isCurrentlyActive)
        } /*end IF*/

    } /*end onChildDraw*/

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        listener.onSwiped(viewHolder, direction, viewHolder.adapterPosition)
    }

    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float = 0.7f

    interface SwipeToDeleteListener {
        fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int, position: Int)
    } /*end Swipe Listener*/

}  /*end SwipeToDelete*/