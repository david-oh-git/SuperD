package io.audioshinigami.superd.downloads

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.audioshinigami.superd.data.source.FileInfoRepository

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