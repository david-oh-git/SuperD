package io.audioshinigami.superd.zdata.source

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import io.audioshinigami.superd.zdata.FileInfo
import io.audioshinigami.superd.zdata.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class DefaultFileInfoRepository(
    private val localFileInfoSource: FileInfoSource,
    private val remoteDownloadDataSource: DownloadDataSource,
    private val ioDispather: CoroutineDispatcher = Dispatchers.IO
): FileInfoRepository {

    override fun observeAll(): LiveData<Result<List<FileInfo>>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAllPaged(): LiveData<PagedList<FileInfo>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAllFileInfo(): Result<List<FileInfo>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun save(vararg fileInfo: FileInfo) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun update(fileInfo: FileInfo) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun update(url: String, progressValue: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun find(id: Int): FileInfo? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun clearCompleted(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun delete(fileInfo: FileInfo) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun delete(url: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun delete(id: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun deleteAll() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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