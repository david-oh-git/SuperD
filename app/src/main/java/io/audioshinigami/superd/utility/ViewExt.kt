package io.audioshinigami.superd.utility

import android.view.View

/* hides view*/
fun View.hideView(){
    this.visibility = View.INVISIBLE
}

/* show view */
fun View.showView(){
    this.visibility = View.VISIBLE
}