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

class SharedViewModel(
    private val repository: DefaultRepository,
    private val _isActive: MutableMap<String, Boolean> = mutableMapOf()
    ) :
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

    private fun loadPagedData(){

        val factory: DataSource.Factory<Int, FileData> = repository.getAllPaged()
        val pagedListBuilder: LivePagedListBuilder<Int, FileData> = LivePagedListBuilder<Int, FileData>(factory,
            CACHED_PAGE_SIZE )

        pagedDownloads = pagedListBuilder.build()

    }

    fun startDownload( url: String ) = viewModelScope.launch(Dispatchers.IO) {

        Timber.d("startDownload called ....")
        /* start download */
        repository.start(url)

        /*add to active url*/
        _isActive[url] = true

        /* add to active downloads*/
        launch(Dispatchers.Main) { _activeDownloads[url] = true }

        enableFetchListener()

        /* get latest data from DB for UI */
//        loadData()
    }

    /* saves updated progress value to DB*/
    private fun updateProgressValue( url: String, progressValue: Int ) = viewModelScope.launch(Dispatchers.IO) {
        repository.update( url, progressValue )
    }

    fun enableFetchListener(){

        /* if fetch instance is closed, exit*/
        if( repository.fetch.isClosed ){
            return
        }


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

                Timber.d("b4 progress : ${download.progress}")

                /* remove url from activeDownload*/
                _activeDownloads.remove( download.url )
                _isActive.remove( download.url )

                /* if error , assign negative value of progress */
                val progress = if ( 0 > download.progress ) download.progress else download.progress * -1

                Timber.d(" Progress is : $progress")
                updateProgressValue( download.url , progress )
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
                if( download.progress > 67 )
                    repository.pause( download.id )
                updateProgressValue( download.url, download.progress )
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



        fetchListener?.apply { repository.fetch.addListener(this) }

    }

    fun disableFetchListener(){

        if( repository.fetch.isClosed )
            return

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

    fun downloadAction(id: Int, url: String , isActive: Boolean? ){

        when( isActive ){
            true -> {
                repository.pause(id)
                _isActive[url] = false
                Timber.d( "pausing download ***** \n")
                Timber.d("Is download active ? ${_isActive[url]}")
            }

            false -> {
                repository.resume(id)
                _isActive[url] = true
                Timber.d("resuming download ***** \n")
                Timber.d("Is download active ? ${_isActive[url]}")
            }

            else -> {
                /* add url to list of active urls*/
                _isActive[url] = true

                viewModelScope.launch(Dispatchers.IO) { repository.restart(url) }
                /* enable listener*/
                enableFetchListener()

                Timber.d("restarting download ***** \n")
                Timber.d("Is download active ? ${_isActive[url]}")
            }
        }
    }

    fun delete( fileData: FileData) = viewModelScope.launch(Dispatchers.IO){
        repository.delete( fileData )
    }

    fun delete( url: String ) = viewModelScope.launch(Dispatchers.IO){
        // TODO : also remove from queue if downloading
        repository.delete( url )
    }

    private fun createSuccess( data: List<FileData>): Result.Success<List<FileData>> {
        return Result.Success( data )

    }

    companion object {
        private const val CACHED_PAGE_SIZE = 15
    }
}