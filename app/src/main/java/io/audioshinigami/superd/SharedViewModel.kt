package io.audioshinigami.superd

import androidx.lifecycle.*
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

    /* list of currently active downloads*/
    private val _activeDownloads = arrayListOf<String>()
    val isActive: (String) -> LiveData<Boolean>
    get() = ::isDownloadActive

    init {

        /* get data from DB */
        loadData()
    }

    fun isDownloadActive(url : String ): MutableLiveData<Boolean> {

        return MutableLiveData(url in _activeDownloads)
    }

    fun startDownload( url: String ) = viewModelScope.launch(Dispatchers.IO) {

        /* start download */
        repository.start(url)

        /* add to active downloads*/
        launch(Dispatchers.Main) { activeDownloads.add(url) }
        // TODO attach fetch listener
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
                _downloads.postValue(Transformations.map(dbLiveData, ::createSuccess) )
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
    }
}