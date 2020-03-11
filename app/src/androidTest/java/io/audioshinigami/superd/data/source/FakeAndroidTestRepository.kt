package io.audioshinigami.superd.data.source

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.tonyodev.fetch2.FetchListener
import io.audioshinigami.superd.util.FileInfoAndroidFactory
import io.audioshinigami.superd.data.FileInfo
import io.audioshinigami.superd.data.Result
import io.audioshinigami.superd.data.Result.Error
import io.audioshinigami.superd.data.Result.Success

class FakeAndroidTestRepository : FileInfoRepository {

    private var fileInfoDataService: LinkedHashMap<Int, FileInfo> = LinkedHashMap()

    private val observableFileInfos = MutableLiveData<Result<List<FileInfo>>>()

    private var shouldReturnError = false

    private var activeDownloads = mutableMapOf<Int, Boolean>()

    fun setReturnError(value: Boolean) {
        shouldReturnError = value
    }

    override suspend fun getAllFileInfo(): Result<List<FileInfo>> {
        if( shouldReturnError )
            return Error( Exception("Test exception") )

        return Success( fileInfoDataService.values.toList() )
    }

    override suspend fun observeAll(): LiveData<Result<List<FileInfo>>> {
        observableFileInfos.value = getAllFileInfo()

        return observableFileInfos
    }

    override fun getAllPaged(): LiveData<PagedList<FileInfo>> {
        return MutableLiveData<PagedList<FileInfo>>()
    }

    override suspend fun save(vararg fileInfo: FileInfo) {

        for( singleFileInfo in fileInfo ){
            fileInfoDataService[ singleFileInfo.request_id ] = singleFileInfo
        }
    }

    override suspend fun update(fileInfo: FileInfo) {
        fileInfoDataService[ fileInfo.request_id ] = fileInfo
    }

    override suspend fun update(url: String, progressValue: Int) {
        val fileInfo = fileInfoDataService.values.filter { it.url == url }[0].copy( progressValue = progressValue)

        fileInfoDataService.replace(fileInfo.request_id, fileInfo)
    }

    override suspend fun find(id: Int): FileInfo? {
        return fileInfoDataService[id]
    }

    override suspend fun clearCompleted(): Int {
        val size = fileInfoDataService.filterValues { it.progressValue == 100 }.size

        fileInfoDataService = fileInfoDataService.filterValues { it.progressValue != 100
        } as LinkedHashMap<Int, FileInfo>

        return size
    }

    override suspend fun delete(fileInfo: FileInfo) {
        delete( fileInfo.request_id )
    }

    override suspend fun delete(url: String) {
        val toDelete = fileInfoDataService.values.filter { it.url == url }[0]

        delete( toDelete.request_id )
    }

    override suspend fun delete(id: Int) {
        fileInfoDataService.remove( id )
    }

    override suspend fun deleteAll() {
        fileInfoDataService.clear()
    }

    override suspend fun start(url: String, downloadUri: Uri) {
        val fileInfo = FileInfoAndroidFactory.singleEntry().copy( url = url)
        fileInfoDataService[fileInfo.request_id] = fileInfo
        activeDownloads[fileInfo.request_id] = true
    }

    override suspend fun restart(url: String, downloadUri: Uri) {
    }

    override fun pause(id: Int) {
        activeDownloads[id] = false
    }

    override fun resume(id: Int) {
    }

    override suspend fun setListener(fetchListener: FetchListener) {
    }

    override fun disableListener() {
    }

    override fun isDownloading(): Boolean {
        if( activeDownloads.isEmpty())
            return false

        for( value in activeDownloads.values ){
            if( value )
                return true
        }

        return false
    }

    override suspend fun find(url: String): FileInfo? {
        return fileInfoDataService.values.filter { it.url == url }[0]
    }

    override fun hasActiveListener(): Boolean {
        return activeDownloads.isNotEmpty()
    }

    override fun addState(id: Int, state: State) {
    }
}