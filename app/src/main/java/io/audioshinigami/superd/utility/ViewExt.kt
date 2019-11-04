package io.audioshinigami.superd.utility

import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

/* hides view*/
fun View.hideView(){
    this.visibility = View.INVISIBLE
}

/* show view */
fun View.showView(){
    this.visibility = View.VISIBLE
}

/* create a snackbar*/
fun Fragment.sendSnack( message: String ){
    Snackbar.make( this.requireView(), message, Snackbar.LENGTH_LONG).show()
}