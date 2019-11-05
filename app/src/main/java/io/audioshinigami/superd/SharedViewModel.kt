package io.audioshinigami.superd

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.audioshinigami.superd.common.subscribe
import io.audioshinigami.superd.data.Result
import io.audioshinigami.superd.data.repository.DefaultRepository
import io.audioshinigami.superd.data.source.db.entity.FileData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class SharedViewModel( private val repository: DefaultRepository) :
    ViewModel() {
    // TODO: Implement the ViewModel

    private val _downloads = MutableLiveData<Result<List<FileData>>>().apply { value = Result.Loading }
    val downloads: LiveData<Result<List<FileData>>> = _downloads

    /* list of currently active downloads*/
    var activeDownloads = arrayListOf<String>()

    init {

        /* get data from DB */
        loadData()
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
                val data = Result.Success( repository.getAll().value!! )
                _downloads.postValue( data )
            }

            catch (e : Exception ){
                _downloads.postValue( Result.Error(Exception("Data not found !")) )
            }

        }

    } //END loadData

    companion object {
        private const val TAG = "SharedView"
    }
}