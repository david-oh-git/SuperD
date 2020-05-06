package io.audioshinigami.superd.data.source

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.tonyodev.fetch2.FetchListener
import io.audioshinigami.superd.data.FileInfo
import io.audioshinigami.superd.data.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DefaultRepository @Inject constructor(
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
        localFileInfoSource.update(url, progressValue)
    }

    override suspend fun find(id: Int): FileInfo? = withContext(ioDispatcher){
        return@withContext localFileInfoSource.find(id)
    }

    override suspend fun find(url: String): FileInfo? = withContext(ioDispatcher){
        return@withContext localFileInfoSource.find(url)
    }

    override suspend fun clearCompleted() = withContext(ioDispatcher){
        return@withContext localFileInfoSource.deleteCompletedFileData()
    }

    override suspend fun delete(fileInfo: FileInfo) {
        localFileInfoSource.delete(fileInfo)
    }

    override suspend fun delete(url: String) {
        localFileInfoSource.delete(url)
    }

    override suspend fun delete(id: Int) {
        localFileInfoSource.delete(id)
    }

    override suspend fun deleteAll() {
        localFileInfoSource.deleteAll()
    }

    override suspend fun start(url: String, downloadUri: Uri) = withContext(ioDispatcher){
        return@withContext remoteDownloadDataSource.start(url , downloadUri )
    }

    override suspend fun restart(url: String, downloadUri: Uri) {
        remoteDownloadDataSource.restart(url, downloadUri)
    }

    override suspend fun setListener(fetchListener: FetchListener)= withContext(ioDispatcher) {
        remoteDownloadDataSource.setListener(fetchListener)
    }

    override fun disableListener() {
        remoteDownloadDataSource.disableListener()
    }

    override fun pause(id: Int) {
        remoteDownloadDataSource.pause(id)
    }

    override fun resume(id: Int) {
        remoteDownloadDataSource.resume(id)
    }

    override fun isDownloading() = remoteDownloadDataSource.isDownloading()

    override fun hasActiveListener() = remoteDownloadDataSource.hasActiveListener()

    override fun addState(id: Int, state: State) {
        remoteDownloadDataSource.addState(id, state)
    }
}