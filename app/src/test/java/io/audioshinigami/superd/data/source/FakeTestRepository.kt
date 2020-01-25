package io.audioshinigami.superd.data.source

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.tonyodev.fetch2.FetchListener
import io.audioshinigami.superd.util.FileInfoFactory
import io.audioshinigami.superd.zdata.FileInfo
import io.audioshinigami.superd.zdata.Result
import io.audioshinigami.superd.zdata.Result.Error
import io.audioshinigami.superd.zdata.Result.Success
import io.audioshinigami.superd.zdata.source.FileInfoRepository

class FakeTestRepository : FileInfoRepository {

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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun delete(id: Int) {
        fileInfoDataService.remove( id )
    }

    override suspend fun deleteAll() {
        fileInfoDataService.clear()
    }

    override suspend fun start(url: String, downloadUri: Uri) {
        val fileInfo = FileInfoFactory.singleEntry().copy( url = url)
        fileInfoDataService[fileInfo.request_id] = fileInfo
        activeDownloads[fileInfo.request_id] = true
    }

    override suspend fun restart(url: String, downloadUri: Uri) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun pause(id: Int) {
        activeDownloads[id] = false
    }

    override fun resume(id: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun setListener(fetchListener: FetchListener) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun disableListener() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun hasActiveListener(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onError(id: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}