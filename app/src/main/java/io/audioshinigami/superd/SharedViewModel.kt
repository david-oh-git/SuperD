package io.audioshinigami.superd

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tonyodev.fetch2.Fetch
import io.audioshinigami.superd.data.Result
import io.audioshinigami.superd.data.repository.DefaultRepository
import io.audioshinigami.superd.data.source.db.entity.FileData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SharedViewModel( private val repository: DefaultRepository,
                       private val fetch: Fetch ) :
    ViewModel() {
    // TODO: Implement the ViewModel

    private val _downloads = MutableLiveData<Result<List<FileData>>>()
    val downloads: LiveData<Result<List<FileData>>> = _downloads

    /* list of currently active downloads*/
    private var activeDownloads = MutableLiveData<MutableList<String>>()

    val cat = mutableListOf<String>()

    fun startDownload( url: String ) = viewModelScope.launch(Dispatchers.IO) {

        /* start download */
        repository.start(url)

        /* add to active downloads*/
        activeDownloads.value?.add(url)

        // TODO attach fetch listener

    }

    fun pauseDownload( id: String ){

    }

    fun resumeDownload( id: String ){

    }
}