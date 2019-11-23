package io.audioshinigami.superd.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.tonyodev.fetch2.Download
import com.tonyodev.fetch2.Error
import com.tonyodev.fetch2.FetchListener
import com.tonyodev.fetch2core.DownloadBlock
import io.audioshinigami.superd.App
import io.audioshinigami.superd.data.source.db.entity.FileData
import io.audioshinigami.superd.repository.FileRepository
import kotlinx.coroutines.*
import timber.log.Timber

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
    } /*end updateProgressvalue*/

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
                Timber.d( "download request_id is ${download.id}")
                if(waitingOnNetwork){
                    Timber.d( "onQueued: waiting 4 network")
                }

            }

            override fun onCompleted(download: Download) {
                Timber.d( "Download 100%")
                //TODO updateProgressvalue db value

            }

            override fun onDownloadBlockUpdated(download: Download, downloadBlock: DownloadBlock, totalBlocks: Int) {
                Timber.d( "onDownloadBlockUpdated: ")
            }

            override fun onError(download: Download, error: Error, throwable: Throwable?) {
                Timber.d( "onError: ${error.value}")
                Timber.d( "Throwable: "+error.throwable?.message)

            }

            override fun onProgress(download: Download, etaInMilliSeconds: Long, downloadedBytesPerSecond: Long) {
                /*updates the progressbar */
                Timber.d( "onProgress" )
                Timber.d( "progress value : ${download.progress} ")
                //TODO updateProgressvalue progress bar
                progressListener?.let { function ->
                    function(download.progress, download.url)
                }


            }

            override fun onPaused(download: Download) {
//                TODO: when paused button icon doesnt change
                Timber.d("Download paused" )
                //TODO when pasued updateProgressvalue db progress value
            }

            override fun onResumed(download: Download) {
                Timber.d( "Download resumed" )
            }

            override fun onCancelled(download: Download) {
                Timber.d( " Cancelled")

            }

            override fun onRemoved(download: Download) {
                Timber.d( "onRemoved:")
            }

            override fun onDeleted(download: Download) {
                Timber.d( "onDeleted:")
            }

            override fun onAdded(download: Download) {
                Timber.d( "onAdded:")
            }


            override fun onStarted(download: Download, downloadBlocks: List<DownloadBlock>, totalBlocks: Int) {
                Timber.d( "onStarted:")
            }

            override fun onWaitingNetwork(download: Download) {
                Timber.d(  "onWaitingNetwork:")
            }
        } //end listener

        App.instance.fetch?.addListener(fetchListener!!)
        

    } //end fetchListenerActive

    fun removeFetchListner(){
        if( fetchListener != null )
            App.instance.fetch?.removeListener(fetchListener!!)
    } /*end removeFetch*/





} /*end FileDataviewModel*/