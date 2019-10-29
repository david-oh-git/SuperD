package io.audioshinigami.superd

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tonyodev.fetch2.Fetch
import io.audioshinigami.superd.data.Result
import io.audioshinigami.superd.data.repository.DefaultRepository
import io.audioshinigami.superd.data.source.db.entity.FileData

class SharedViewModel( private val repository: DefaultRepository,
                       private val fetch: Fetch ) :
    ViewModel() {
    // TODO: Implement the ViewModel

    private val _downloads = MutableLiveData<Result<List<FileData>>>()
    val downloads: LiveData<Result<List<FileData>>> = _downloads

    fun startDownload( url: String ){

    }

    fun pauseDownload( id: String ){

    }

    fun resumeDownload( id: String ){

    }
}