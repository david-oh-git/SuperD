package io.audioshinigami.superd.adddownload

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import io.audioshinigami.superd.data.source.FileInfoRepository
import kotlinx.coroutines.launch

class AddFileInfoViewModel(
    private val fileInfoRepository: FileInfoRepository
): ViewModel() {

    internal fun startDownload( url: String , downloadUri:Uri )
            = viewModelScope.launch {

        fileInfoRepository.start(url, downloadUri)
    }
}

@Suppress("UNCHECKED_CAST")
class AddDownloadViewModelFactory(
    private val fileInfoRepository: FileInfoRepository
): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        ( AddFileInfoViewModel( fileInfoRepository) as T)
}