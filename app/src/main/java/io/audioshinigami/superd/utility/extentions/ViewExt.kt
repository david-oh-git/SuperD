package io.audioshinigami.superd.utility.extentions

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import io.audioshinigami.superd.R

/* hides view*/
fun View.hideView(){
    this.visibility = View.INVISIBLE
}

/* show view */
fun View.showView(){
    this.visibility = View.VISIBLE
}
