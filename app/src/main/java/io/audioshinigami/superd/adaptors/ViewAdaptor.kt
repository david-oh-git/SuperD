package io.audioshinigami.superd.adaptors


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import io.audioshinigami.superd.App
import io.audioshinigami.superd.R
import io.audioshinigami.superd.common.DOWNLOAD_COMPLETE_VALUE
import io.audioshinigami.superd.activities.HomeActivity
import io.audioshinigami.superd.data.db.entity.FileData
import io.audioshinigami.superd.viewholders.ItemViewHolder
import io.audioshinigami.superd.viewmodels.FileDataViewModel

class ViewAdaptor : RecyclerView.Adapter<ItemViewHolder>(){

    private var data = mutableListOf<FileData>()
    private var viewModel: FileDataViewModel? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder{
        val view =  LayoutInflater.from(parent.context).inflate(R.layout.download_item_2, parent, false)
        viewModel = ViewModelProviders.of(parent.context as HomeActivity).get(FileDataViewModel::class.java)

        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(itemHolder: ItemViewHolder, position: Int) {
        if(data.isNotEmpty()){

            val currentData = data[position]
            val downloadActive: Boolean? = App.instance.activeDownloads?.get(currentData.url)
            itemHolder.fileNameTxtVw.text = currentData.fileName
            itemHolder.progressBar.progress = currentData.progressValue
            itemHolder.requestId = currentData.request_id
            itemHolder.url = currentData.url


            /*if already completed set icon to complete */
            if(currentData.progressValue == DOWNLOAD_COMPLETE_VALUE){
                itemHolder.pausePlayBtn.setImageResource(R.drawable.ic_done)
            } /*end if*/
            else{

                /*checks if download is active & places correct icon*/
                if(downloadActive == true){
                    itemHolder.pausePlayBtn.setImageResource(R.drawable.ic_pause)
                } /*end if*/
                else if( downloadActive == false || downloadActive == null ){
                    itemHolder.pausePlayBtn.setImageResource(R.drawable.ic_play)
                } /*end else if*/

            } /*end else */

            viewModel?.setProgressListener { progress, url ->
                updateProgressBar(progress, url)
            }

            itemHolder.pausePlayBtn.setOnClickListener {

                if( currentData.progressValue != DOWNLOAD_COMPLETE_VALUE){
                    playPauseButtonOnClick(currentData, itemHolder)
                } /*end if*/

            }

        } /*end if*/

    } /*end onBindViewHolder*/

    override fun getItemCount(): Int = if(data.isNotEmpty()) data.size else 0

    fun addItems(data: List<FileData>){
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    } //end addItems

    private fun playPauseButtonOnClick(fileData: FileData, holder: ItemViewHolder){

        when (App.instance.activeDownloads?.get(fileData.url)) {
            true -> {
                viewModel?.pauseDownLoad(fileData.request_id, fileData.url)
                holder.pausePlayBtn.setImageResource(R.drawable.ic_play)
            }
            false -> {
                viewModel?.resumeDownload(fileData.request_id, fileData.url)
                holder.pausePlayBtn.setImageResource(R.drawable.ic_pause)
            }
            null -> {
                viewModel?.restartDownload(fileData.url)
                holder.pausePlayBtn.setImageResource(R.drawable.ic_pause)
            }
        } /*end when*/

    } /*end playPauseButtonOnClick*/

    private fun updateProgressBar(progressValue: Int, url: String) {
        /*searches fileData & updates progressValue & notify RecylerView*/
        if(data.isNotEmpty()){
            val position = data.find { it.url == url }.run { data.indexOf(this) }
            if( position != -1 ){
                data[position] = data[position].copy(progressValue = progressValue)
                notifyItemChanged(position)
            }

        } /*end if*/

    } /*end updateProgressBar*/

    fun getItem(position: Int) : FileData = data[position]

    fun removeItem(position: Int){
        data.removeAt(position)
        notifyItemChanged(position)
    }  /*end removeItem*/

    fun restore(fileData: FileData, position: Int){
        data.add(position, fileData)
        notifyItemChanged(position)
    } /*end restore*/

}/*end ViewAdaptor*/