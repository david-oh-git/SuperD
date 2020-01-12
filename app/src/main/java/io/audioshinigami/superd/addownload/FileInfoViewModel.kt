package io.audioshinigami.superd.addownload

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.audioshinigami.superd.zdata.source.FileInfoRepository

class FileInfoViewModel(
    private val fileInfoRepository: FileInfoRepository
): ViewModel() {
}

@Suppress("UNCHECKED_CAST")
class FileInfoViewModelFactory(
    private val fileInfoRepository: FileInfoRepository
): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        ( FileInfoViewModel( fileInfoRepository) as T)
}