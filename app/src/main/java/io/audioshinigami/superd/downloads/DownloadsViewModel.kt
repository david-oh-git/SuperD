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
import io.audioshinigami.superd.zdata.FileInfo
import io.audioshinigami.superd.zdata.source.FileInfoRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.File

class DownloadsViewModel(
    private val fileInfoRepository: FileInfoRepository
): ViewModel() {

    private val _pagedDownloads = fileInfoRepository.getAllPaged()
    val pagedDownloads: LiveData<PagedList<FileInfo>> = _pagedDownloads

    fun downloadAction(id: Int, url: String , isActive: Boolean?) = viewModelScope.launch{

        Timber.d("isactive is $isActive")
        when( isActive ){
            true -> {
                fileInfoRepository.pause(id)
                Timber.d("pausing download")
            }
            false -> {
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
                }

                override fun onQueued(download: Download, waitingOnNetwork: Boolean) {

                }

                override fun onAdded(download: Download) {
                    Timber.d("download added...")
                }

                override fun onCancelled(download: Download) {
                }

                override fun onDeleted(download: Download) {
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
                        fileInfoRepository.onError(download.id)
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
//
                    if( download.progress > 10 )
                        fileInfoRepository.pause(download.id)

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