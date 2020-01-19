package io.audioshinigami.superd.downloads

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import io.audioshinigami.superd.zdata.FileInfo
import io.audioshinigami.superd.zdata.source.FileInfoRepository
import kotlinx.coroutines.launch
import timber.log.Timber

class DownloadsViewModel(
    private val fileInfoRepository: FileInfoRepository
): ViewModel() {

    private val _pagedDownloads = fileInfoRepository.getAllPaged()
    val pagedDownloads: LiveData<PagedList<FileInfo>> = _pagedDownloads

    fun downloadAction(id: Int, url: String , isActive: Boolean?) = viewModelScope.launch{

        when( isActive ){
            true -> {
                fileInfoRepository.pause(id)
                Timber.d("pausing download")
            }
            false -> {
                fileInfoRepository.resume(id)
                Timber.d("Resuming download")
            }
            else -> {
                // TODO add uri to fileInfo to proceed
                val fileInfo = fileInfoRepository.find(id)
                fileInfo?.run {

                }
            }
        }
    }


}

@Suppress("UNCHECKED_CAST")
class DownloadsViewModelFactory(
    private val fileInfoRepository: FileInfoRepository
): ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        ( DownloadsViewModel( fileInfoRepository ) as T)
}