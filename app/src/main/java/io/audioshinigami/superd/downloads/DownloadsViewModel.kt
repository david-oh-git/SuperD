package io.audioshinigami.superd.downloads

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import com.tonyodev.fetch2.Download
import com.tonyodev.fetch2.Error
import com.tonyodev.fetch2.FetchListener
import com.tonyodev.fetch2core.DownloadBlock
import io.audioshinigami.superd.utility.ReUseMethods
import io.audioshinigami.superd.data.FileInfo
import io.audioshinigami.superd.data.source.FileInfoRepository
import io.audioshinigami.superd.data.source.State
import io.audioshinigami.superd.data.source.State.*
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File

class DownloadsViewModel(
    private val fileInfoRepository: FileInfoRepository
): ViewModel() {

    val snackBarMessage = OneTimeLiveData<SnackMessage>()

    private val _pagedDownloads = fileInfoRepository.getAllPaged()
    val pagedDownloads: LiveData<PagedList<FileInfo>> = _pagedDownloads

    fun downloadAction(id: Int, url: String , isActive: State? ) = viewModelScope.launch{

        when( isActive ){
            DOWNLOADING -> {
                fileInfoRepository.pause(id)
                Timber.d("pausing download")
            }
            PAUSED -> {
                fileInfoRepository.resume(id)
                Timber.d("Resuming download")
            }
            else -> {
                // TODO get uri from user
                val uri = Uri.parse( ReUseMethods.getPublicFileStorageDir().toString() +
                        File.separator + url.substringAfterLast("/") )

                Timber.d("uri is $uri")

                fileInfoRepository.restart(url, uri)
                Timber.d("restarting for $url")
                enableFetchListener()

            }
        }
    }
    fun delete(url: String)= viewModelScope.launch {
        fileInfoRepository.delete(url)
    }

    fun enableFetchListener(){
        // if a download is active then create and attach a listener

        if( fileInfoRepository.isDownloading() && !fileInfoRepository.hasActiveListener() ){
            Timber.d("Enabling fetchListener !!")
            val fetchListener = object : FetchListener {

                override fun onCompleted(download: Download) {
                    Timber.d("download completed...")
                    viewModelScope.launch {
                        fileInfoRepository.addState(download.id , COMPLETE)
                        fileInfoRepository.update(download.url, 100)
                    }

                    snackBarMessage.sendData( SnackMessage("Download complete") )
                }

                override fun onQueued(download: Download, waitingOnNetwork: Boolean) {
                    Timber.d("download Queued...")
                }

                override fun onAdded(download: Download) {
                    Timber.d("download added...")
                }

                override fun onCancelled(download: Download) {
                    Timber.d("download cancelled...")
                }

                override fun onDeleted(download: Download) {
                    Timber.d("download deleted...")
                }

                override fun onDownloadBlockUpdated(
                    download: Download,
                    downloadBlock: DownloadBlock,
                    totalBlocks: Int
                ) {
                }

                override fun onError(download: Download, error: Error, throwable: Throwable?) {
                    Timber.e(  "onError: error code is ${error.value}")
                    Timber.e(  "onError: msg is $error")
                    Timber.e(  " url is : ${download.url}")

                    Timber.e("b4 progress : ${download.progress}")

                    /* if error , assign negative value of progress */
                    val progress = if ( 0 > download.progress ) download.progress else download.progress * -1

                    Timber.e(" Progress is : $progress")
                    viewModelScope.launch {
                        fileInfoRepository.update( download.url, download.progress)
                        fileInfoRepository.addState(download.id, ERROR)
                    }
                }

                override fun onPaused(download: Download) {
                    Timber.d("Paused ")
                }

                override fun onProgress(
                    download: Download,
                    etaInMilliSeconds: Long,
                    downloadedBytesPerSecond: Long
                ) {
                    Timber.d( "progress : ${download.progress}% .... ")

                    viewModelScope.launch {
                        fileInfoRepository.update( download.url, download.progress)
                    }
                }

                override fun onRemoved(download: Download) {
                }

                override fun onResumed(download: Download) {
                    Timber.d("Resumed")
                }

                override fun onStarted(
                    download: Download,
                    downloadBlocks: List<DownloadBlock>,
                    totalBlocks: Int
                ) {
                }

                override fun onWaitingNetwork(download: Download) {
                    Timber.d("download waiting for network...")
                }
            } //END FetchListener

            // add the listener to fetch
            viewModelScope.launch {
                fileInfoRepository.setListener(fetchListener)
            }

        }// END IF

    }

    fun disableFetchListener(){
        fileInfoRepository.disableListener()
    }

}

@Suppress("UNCHECKED_CAST")
class DownloadsViewModelFactory(
    private val fileInfoRepository: FileInfoRepository
): ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        ( DownloadsViewModel( fileInfoRepository ) as T)
}

data class SnackMessage(val message: String )