package io.audioshinigami.superd

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.tonyodev.fetch2.Download
import com.tonyodev.fetch2.Error
import com.tonyodev.fetch2.FetchListener
import com.tonyodev.fetch2core.DownloadBlock
import io.audioshinigami.superd.data.Result
import io.audioshinigami.superd.data.repository.DefaultRepository
import io.audioshinigami.superd.data.source.db.entity.FileData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class SharedViewModel( private val repository: DefaultRepository) :
    ViewModel() {

    private val _downloads = MutableLiveData<Result<List<FileData>>>().apply { value = Result.Loading }
    val downloads: LiveData<Result<List<FileData>>> = _downloads

    lateinit var pagedDownloads: LiveData<PagedList<FileData>>
    private set

    /* list of currently active downloads*/
    private val _activeDownloads = mutableMapOf<String, Boolean>()

    /* fetch listener*/
    var fetchListener: FetchListener? = null


    init {
        
        /* get paged data */
        loadPagedData()
        /* get data from DB */
//        loadData()
    }

    fun loadPagedData(){

        val factory: DataSource.Factory<Int, FileData> = repository.getAllPaged()
        val pagedListBuilder: LivePagedListBuilder<Int, FileData> = LivePagedListBuilder<Int, FileData>(factory,
            CACHED_PAGE_SIZE )

        pagedDownloads = pagedListBuilder.build()
    }

    fun startDownload( url: String ) = viewModelScope.launch(Dispatchers.IO) {

        Timber.d("startDownload called ....")
        /* start download */
        repository.start(url)

        /* add to active downloads*/
        launch(Dispatchers.Main) { _activeDownloads[url] = true }

        enableFetchListener()

        /* get latest data from DB for UI */
//        loadData()
    }

    /* saves updated progress value to DB*/
    private fun updateProgressValue( url: String, progressValue: Int ) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateProgressvalue( url, progressValue )
    }

    fun enableFetchListener(){

        /* if not null, add fetchListener*/
        fetchListener?.apply {
            repository.fetch.addListener(this)
            return
        }

        fetchListener = object : FetchListener {

            override fun onCompleted(download: Download) {

            }

            override fun onQueued(download: Download, waitingOnNetwork: Boolean) {

            }

            override fun onAdded(download: Download) {

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
                Timber.d(  "onError: error code is ${error.value}")
                Timber.d(  "onError: msg is $error")
                Timber.d(  " url is : ${download.url}")

                /* remove url from activeDownload*/
                _activeDownloads.remove( download.url )
            }

            override fun onPaused(download: Download) {
            }

            override fun onProgress(
                download: Download,
                etaInMilliSeconds: Long,
                downloadedBytesPerSecond: Long
            ) {
                Timber.d( "progress : ${download.progress}% .... ")
//
//                if( download.progress > 5 )
//                    repository.pause( download.id )
                updateProgressValue( download.url, download.progress )
            }

            override fun onRemoved(download: Download) {
            }

            override fun onResumed(download: Download) {
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



        fetchListener?.apply { repository.fetch.addListener(this) }

    }

    fun disableFetchListener(){

        /* remove fetchListener */
        fetchListener?.apply {
            repository.fetch.removeListener(this)
        }

    }

    private fun loadData() = viewModelScope.launch(Dispatchers.Main){
        /* loads data from DB */
        launch(Dispatchers.IO) {


            try {

                val data = repository.getData()
                _downloads.postValue( Result.Success(data) )
            }

            catch (e : Exception ){
                _downloads.postValue( Result.Error(Exception("Data not found !")) )
            }

        }

    } //END loadData

    fun downloadAction(id: Int, url: String ){

        when( _activeDownloads[url] ){
            true -> {
                repository.pause(id)
                _activeDownloads[url] = false
                Timber.d( "pausing download ***** \n")
            }

            false -> {
                repository.resume(id)
                _activeDownloads[url] = true
                Timber.d("resuming download ***** \n")
            }

            else -> {
                viewModelScope.launch(Dispatchers.IO) { repository.restart(url) }
                enableFetchListener()

                /* add url to list of active urls*/
                _activeDownloads[url] = true

                Timber.d("restarting download ***** \n")
            }
        }
    }

    private fun createSuccess( data: List<FileData>): Result.Success<List<FileData>> {
        return Result.Success( data )

    }

    companion object {
        private const val CACHED_PAGE_SIZE = 15
    }
}