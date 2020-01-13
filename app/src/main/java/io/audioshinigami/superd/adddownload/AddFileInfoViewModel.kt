package io.audioshinigami.superd.adddownload

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.audioshinigami.superd.zdata.source.FileInfoRepository

class AddFileInfoViewModel(
    private val fileInfoRepository: FileInfoRepository
): ViewModel() {

    internal fun startDownload(){
        // TODO fetchListener conundrum
    }
}

@Suppress("UNCHECKED_CAST")
class AddFileInfoViewModelFactory(
    private val fileInfoRepository: FileInfoRepository
): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        ( AddFileInfoViewModel( fileInfoRepository) as T)
}