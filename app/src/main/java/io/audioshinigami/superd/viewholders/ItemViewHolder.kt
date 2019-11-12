package io.audioshinigami.superd.viewholders

import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import io.audioshinigami.superd.R

class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    /*ViewHolder for each download item */
    var requestId : Int? = null
    var fileNameTxtVw: TextView = view.findViewById(R.id.id_filename) as TextView
    var progressBar: ProgressBar = view.findViewById(R.id.id_dwloadprogressbar)
    var pausePlayBtn: ImageView = view.findViewById(R.id.id_play_pause)
    var backgroundView: RelativeLayout = view.findViewById(R.id.id_view_background)
    var foregroundView: ConstraintLayout = view.findViewById(R.id.id_view_forground)
    var url: String = ""
    
} /*end ItemViewHolder*/