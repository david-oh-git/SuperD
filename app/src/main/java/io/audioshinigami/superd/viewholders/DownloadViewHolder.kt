package io.audioshinigami.superd.viewholders



import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.audioshinigami.superd.R

class DownloadViewHolder(view: View): RecyclerView.ViewHolder(view), View.OnClickListener {

    private var mPosition : Int? = null
    private var requestId : Int? = null
    private var fileNameTxtVw: TextView
    private var progressBar: ProgressBar

    init {
        view.setOnClickListener(this)
        fileNameTxtVw = view.findViewById(R.id.id_filename_txtvw) as TextView
        progressBar = view.findViewById(R.id.id_progressbar_download) as ProgressBar
    }

    override fun onClick(v: View?) {

    }
}  //end Download