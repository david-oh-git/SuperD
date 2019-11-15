package io.audioshinigami.superd.adaptors
//
//
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ImageView
//import android.widget.ProgressBar
//import android.widget.TextView
//import androidx.recyclerview.widget.RecyclerView
//import com.tonyodev.fetch2.Status
//import io.audioshinigami.superd.activities.HomeActivity
//import io.audioshinigami.superd.R
//import io.audioshinigami.superd.listeners.DownloadListener
//import io.audioshinigami.superd.data.source.db.entity.FileData
//import io.audioshinigami.superd.listeners.ViewHolderInteractionListener
//import io.audioshinigami.superd.models.DownloadStatus
//
//class DownloadAdaptor(_filesData: List<FileData> = emptyList()) : RecyclerView.Adapter<DownloadAdaptor.Companion.ItemViewHolder>(),
//    ViewHolderInteractionListener{
//
//    private var filesData : ArrayList<FileData>
//    private lateinit var view: View
//    private var downloadListener: DownloadListener? = null
//
//    init{
//
//        filesData = ArrayList(_filesData)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
//        view = LayoutInflater.from(parent.context).inflate(R.layout.download_item_2, parent, false)
//
//        if ( parent.context is DownloadListener )
//            downloadListener = parent.context as HomeActivity
//        return ItemViewHolder(view)
//    }
//
//    override fun onBindViewHolder(itemHolder: ItemViewHolder, position: Int) {
//        if( filesData.isNotEmpty() ){
//            val currentData = filesData[position]
//            itemHolder.fileNameTxtVw.text = currentData.fileName
//            itemHolder.progressBar.progress = currentData.progressValue
//            itemHolder.requestId = currentData.request_id
//
//            itemHolder.pausePlayBtn.setOnClickListener {
//
//                if(itemHolder.resume){
//                    downloadListener?.onResumeDownload(currentData.request_id)
//                    itemHolder.resume = false
//                    itemHolder.pausePlayBtn.setImageResource(R.drawable.ic_pause)
//                }else{
//                    downloadListener?.onPauseDownload(currentData.request_id)
//                    itemHolder.resume = true
//                    itemHolder.pausePlayBtn.setImageResource(R.drawable.ic_play)
//                }
//            }
//        } //end if
//
//    } /*end on bind*/
//
//    override fun getItemCount(): Int = if ( filesData.isEmpty() ) 0 else filesData.size
//
//    fun addDownload(fileData: FileData){
//        filesData.add(fileData)
//        notifyItemInserted(filesData.size -1)
//    } //end addDownload
//
//    override fun updateButtonIcon(status: DownloadStatus, request_id: Int) {
//
//    }
//
//    override fun updateProgressBar(progressValue: Int, request_id: Int) {
//        /*so fetch listener can updateProgressvalue current view holder data :) */
//        for(dataIndex in 0 until filesData.size){
//            val currentFileData = filesData[dataIndex]  /*current fileData object in loop */
//            if( currentFileData.request_id == request_id ){
//                val newFileData = FileData(currentFileData.uid,currentFileData.request_id,currentFileData.url,
//                    currentFileData.fileName,currentFileData.statusValue,
//                    if (progressValue == -1 ) 0 else progressValue) /* new object with updated progress value*/
//                filesData.add(dataIndex, newFileData)
//                notifyItemChanged(dataIndex)
//            } //end if
//        } // end for
//    } //end updateProgressBar
//
//
//    companion object{
//
//        class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
//
//            private var mPosition : Int? = null
//            var requestId : Int? = null
//            var fileNameTxtVw: TextView = view.findViewById(R.request_id.id_filename) as TextView
//            var fileName: String? = null
//            var progressBar: ProgressBar = view.findViewById(R.request_id.id_dwloadprogressbar)
//            var pausePlayBtn: ImageView = view.findViewById(R.request_id.id_play_pause)
//            var resume: Boolean = false /* if true download is paused */
//
//        } //end ItemVH
//    }
//} //end DownloadAdaptor