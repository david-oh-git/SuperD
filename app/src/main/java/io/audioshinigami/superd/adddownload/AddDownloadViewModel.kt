package io.audioshinigami.superd.adddownload

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.audioshinigami.superd.data.source.FileInfoRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class AddDownloadViewModel @Inject constructor(
    private val fileInfoRepository: FileInfoRepository
): ViewModel() {

    internal fun startDownload( url: String , downloadUri:Uri )
            = viewModelScope.launch {

        fileInfoRepository.start(url, downloadUri)

    }
}