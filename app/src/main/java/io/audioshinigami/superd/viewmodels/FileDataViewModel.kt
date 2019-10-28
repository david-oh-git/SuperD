package io.audioshinigami.superd.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.tonyodev.fetch2.Download
import com.tonyodev.fetch2.Error
import com.tonyodev.fetch2.FetchListener
import com.tonyodev.fetch2core.DownloadBlock
import io.audioshinigami.superd.App
import io.audioshinigami.superd.common.TAG
import io.audioshinigami.superd.data.source.db.entity.FileData
import io.audioshinigami.superd.repository.FileRepository
import kotlinx.coroutines.*

class FileDataViewModel(application: Application) : AndroidViewModel(application) {

    val allFileData : LiveData<List<FileData>> = FileRepository.allFileData
    private var progressListener: ( (Int, String) -> Unit )? = null
    private var fetchListener: FetchListener? = null


    fun setProgressListener(block: (Int, String) -> Unit ){
        this.progressListener = block
    }

    fun insert(fileData: FileData) = viewModelScope.launch(Dispatchers.IO) {
        FileRepository.insert(fileData)
    } /* end save*/

    fun update( fileData: FileData) = viewModelScope.launch(Dispatchers.IO) {
        FileRepository.update(fileData)
    } /*end updateFileData*/

    fun delete( fileData: FileData) = viewModelScope.launch(Dispatchers.IO) {
        FileRepository.delete(fileData)
    }

    private fun findByUrl( url : String ): Deferred<FileData?> = viewModelScope.async(Dispatchers.IO) {
        FileRepository.findByUrl(url)
    }/*end findByUrl */

    fun findItem(url: String): FileData? = runBlocking {
        findByUrl(url).await()
    }

    fun startDownload(url: String) = viewModelScope.launch(Dispatchers.IO) {
        /*starts download process*/
        FileRepository.startDownload(url)
        if(fetchListener == null )
            enableListener()
    }

    fun restartDownload(url: String) = viewModelScope.launch(Dispatchers.IO) {
        /*starts download process*/
        FileRepository.restartDownload(url)
        if( fetchListener == null )
            enableListener()
    }

    fun pauseDownLoad(id: Int, url: String){
        FileRepository.pauseDownLoad(id, url)
    } /*end pause*/

    fun resumeDownload(id: Int, url: String){
        FileRepository.resumeDownload(id, url)
    }

    fun enableListener(){
        fetchListener = object : FetchListener {

            override fun onQueued(download: Download, waitingOnNetwork: Boolean) {
                Log.d(TAG, "download request_id is ${download.id}")
                if(waitingOnNetwork){
                    Log.d(TAG, "onQueued: waiting 4 network")
                }

            }

            override fun onCompleted(download: Download) {
                Log.d(TAG, "Download 100%")
                //TODO updateFileData db value

            }

            override fun onDownloadBlockUpdated(download: Download, downloadBlock: DownloadBlock, totalBlocks: Int) {
                Log.d(TAG, "onDownloadBlockUpdated: ")
            }

            override fun onError(download: Download, error: Error, throwable: Throwable?) {
                Log.d(TAG, "onError: ${error.value}")
                Log.d(TAG, "Throwable: "+error.throwable?.message)

            }

            override fun onProgress(download: Download, etaInMilliSeconds: Long, downloadedBytesPerSecond: Long) {
                /*updates the progressbar */
                Log.d(TAG, "onProgress" )
                Log.d(TAG, "progress value : ${download.progress} ")
                //TODO updateFileData progress bar
                progressListener?.let { function ->
                    function(download.progress, download.url)
                }

            }

            override fun onPaused(download: Download) {
//                TODO: when paused button icon doesnt change
                Log.d(TAG, "Download paused" )
                //TODO when pasued updateFileData db progress value
            }

            override fun onResumed(download: Download) {
                Log.d(TAG, "Download resumed" )
            }

            override fun onCancelled(download: Download) {
                Log.d(TAG, " Cancelled")

            }

            override fun onRemoved(download: Download) {
                Log.d(TAG, "onRemoved:")
            }

            override fun onDeleted(download: Download) {
                Log.d(TAG, "onDeleted:")
            }

            override fun onAdded(download: Download) {
                Log.d(TAG, "onAdded:")
            }


            override fun onStarted(download: Download, downloadBlocks: List<DownloadBlock>, totalBlocks: Int) {
                Log.d(TAG, "onStarted:")
            }

            override fun onWaitingNetwork(download: Download) {
                Log.d(TAG, "onWaitingNetwork:")
            }
        } //end listener

        App.instance.fetch?.addListener(fetchListener!!)

    } //end fetchListenerActive

    fun removeFetchListner(){
        if( fetchListener != null )
            App.instance.fetch?.removeListener(fetchListener!!)
    } /*end removeFetch*/





} /*end FileDataviewModel*/