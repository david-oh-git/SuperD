package io.audioshinigami.superd

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.tonyodev.fetch2.FetchListener
import io.audioshinigami.superd.data.Result
import io.audioshinigami.superd.data.repository.DefaultRepository
import io.audioshinigami.superd.data.source.db.entity.FileData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SharedViewModel( private val repository: DefaultRepository) :
    ViewModel() {
    // TODO: Implement the ViewModel

    private val _downloads = MutableLiveData<Result<List<FileData>>>().apply { value = Result.Loading }
    val downloads: LiveData<Result<List<FileData>>> = _downloads

    lateinit var pagedDownloads: LiveData<PagedList<FileData>>
    private set

    /* list of currently active downloads*/
    private val _activeDownloads = mutableMapOf<String, Boolean>()

    private val _isActive = MutableLiveData(false)
    val isActive: LiveData<Boolean> = _isActive

    /* updates progressbar value*/
    var setProgressValue: ( ( String, Int ) -> Unit )? = null

    /* fetch listener*/
    var fetchListener: FetchListener? = null


    init {
        
        /* get paged data */
        loadPagedData()
        /* get data from DB */
//        loadData()
    }

    fun isDownloading( url: String): MutableLiveData<Boolean> {
        val isActive = _activeDownloads[url] ?: false

        return MutableLiveData(isActive)
    }

    fun loadPagedData(){

        val factory: DataSource.Factory<Int, FileData> = repository.getAllPaged()
        val pagedListBuilder: LivePagedListBuilder<Int, FileData> = LivePagedListBuilder<Int, FileData>(factory,
            CACHED_PAGE_SIZE )



        pagedDownloads = pagedListBuilder.build()


    }


    fun startDownload( url: String ) = viewModelScope.launch(Dispatchers.IO) {

        /* start download */
        repository.start(url)

        /* add to active downloads*/
        launch(Dispatchers.Main) { _activeDownloads[url] = true }
        // TODO attach fetch listener
    }

    private fun updateProgressValue( url: String, progressValue: Int ){

    }

    fun enableFetchListener(){

    }

    fun disableFetchListener(){

        /* remove fetchListener */
        fetchListener?.apply {
            repository.fetch.removeListener(this)
        }
        
    }

    fun pauseDownload( id: String ){

    }

    fun resumeDownload( id: String ){

    }

    private fun loadData() = viewModelScope.launch(Dispatchers.Main){
        /* loads data from DB */
        launch(Dispatchers.IO) {


            try {

                val dbLiveData = repository.getAll()
                _downloads.postValue( Result.Success(dbLiveData.value!!) )
            }

            catch (e : Exception ){
                _downloads.postValue( Result.Error(Exception("Data not found !")) )
            }

        }

    } //END loadData

    private fun createSuccess( data: List<FileData>): Result.Success<List<FileData>> {
        return Result.Success( data )

    }

    companion object {
        private const val TAG = "SharedView"
        private const val CACHED_PAGE_SIZE = 15
    }
}