package io.audioshinigami.superd.data.source

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import io.audioshinigami.superd.zdata.FileInfo
import io.audioshinigami.superd.zdata.Result
import io.audioshinigami.superd.zdata.Result.Error
import io.audioshinigami.superd.zdata.Result.Success
import io.audioshinigami.superd.zdata.source.FileInfoRepository

class FakeTestRepository : FileInfoRepository {

    private var fileInfoDataService: LinkedHashMap<Int, FileInfo> = LinkedHashMap()

    private val observableFileInfos = MutableLiveData<Result<List<FileInfo>>>()

    private var shouldReturnError = false

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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun restart(url: String, downloadUri: Uri) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun pause(id: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun resume(id: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}