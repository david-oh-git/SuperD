package io.audioshinigami.superd.zdata.source

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import io.audioshinigami.superd.zdata.FileInfo
import io.audioshinigami.superd.zdata.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DefaultFileInfoRepository(
    private val localFileInfoSource: FileInfoSource,
    private val remoteDownloadDataSource: DownloadDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): FileInfoRepository {

    override suspend fun observeAll() = withContext(ioDispatcher) {
        return@withContext localFileInfoSource.observeAll()
    }

    override fun getAllPaged(): LiveData<PagedList<FileInfo>> {
        return localFileInfoSource.getAllPaged()
    }

    override suspend fun getAllFileInfo(): Result<List<FileInfo>> = withContext(ioDispatcher){
        return@withContext localFileInfoSource.getDataResult()
    }

    override suspend fun save(vararg fileInfo: FileInfo) {
        localFileInfoSource.insert( *fileInfo )
    }

    override suspend fun update(fileInfo: FileInfo) {
        localFileInfoSource.update(fileInfo)
    }

    override suspend fun update(url: String, progressValue: Int) {
        //TODO To change body of created functions
    }

    override suspend fun find(id: Int): FileInfo? = withContext(ioDispatcher){
        return@withContext localFileInfoSource.find(id)
    }

    override suspend fun clearCompleted() = withContext(ioDispatcher){
        return@withContext localFileInfoSource.deleteCompletedFileData()
    }

    override suspend fun delete(fileInfo: FileInfo) {
        //TODO To change body of created functions
    }

    override suspend fun delete(url: String) {
        //TODO To change body of created functions
    }

    override suspend fun delete(id: Int) {
        //TODO To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun deleteAll() {
        //TODO To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun start(url: String, downloadUri: Uri) = withContext(ioDispatcher){
        return@withContext remoteDownloadDataSource.start(url , downloadUri )
    }

    override suspend fun restart(url: String, downloadUri: Uri) {
         //TODO To change body of created functions use File | Settings | File Templates.
    }

    override fun pause(id: Int) {
        remoteDownloadDataSource.pause(id)
    }

    override fun resume(id: Int) {
        remoteDownloadDataSource.resume(id)
    }

    override suspend fun isDownloading() = withContext(ioDispatcher) {
        return@withContext remoteDownloadDataSource.isDownloading()
    }
}