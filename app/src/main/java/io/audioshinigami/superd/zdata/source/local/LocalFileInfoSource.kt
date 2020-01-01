package io.audioshinigami.superd.zdata.source.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import io.audioshinigami.superd.zdata.FileInfo
import io.audioshinigami.superd.zdata.Result
import io.audioshinigami.superd.zdata.Result.Error
import io.audioshinigami.superd.zdata.Result.Success
import io.audioshinigami.superd.zdata.source.FileInfoSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LocalFileInfoSource internal constructor(
    private val fileInfoDao: FileInfoDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : FileInfoSource {

    private val CACHED_PAGE_SIZE = 10

    override fun observeAll(): LiveData<Result<List<FileInfo>>> {
        return Transformations.map( fileInfoDao.observeAll() ){
            Success(it)
        }
    }

    override fun getAllPaged(): LiveData<PagedList<FileInfo>> {

        return LivePagedListBuilder<Int, FileInfo>( fileInfoDao.getAllPaged() ,
            CACHED_PAGE_SIZE ).build()
    }

    override suspend fun getDataResult(): Result<List<FileInfo>> = withContext(ioDispatcher){
        return@withContext try {
            Success( fileInfoDao.getData() )
        }catch (e: Exception ){
            Error(e)
        }
    }

    override fun observeFileData(id: Int): LiveData<Result<FileInfo>> {
        return Transformations.map( fileInfoDao.observeFileInfo(id) ){
            Success(it)
        }
    }

    override suspend fun find(id: Int): FileInfo? = withContext(ioDispatcher){
        return@withContext fileInfoDao.find(id)
    }

    override suspend fun find(getUrl: String): FileInfo? = withContext(ioDispatcher){
        return@withContext fileInfoDao.find(getUrl)
    }

    override suspend fun insert(vararg allFileData: FileInfo) = withContext(ioDispatcher) {
        fileInfoDao.insert( *allFileData )
    }

    override suspend fun update(fileInfo: FileInfo) = withContext(ioDispatcher){
        fileInfoDao.update(fileInfo)
    }

    override suspend fun update(url: String, progress: Int) = withContext(ioDispatcher){
        fileInfoDao.update(url, progress)
    }

    override suspend fun updateRequestId(url: String, id: Int) = withContext(ioDispatcher){
        fileInfoDao.updateRequestId(url, id )
    }

    override suspend fun delete(fileInfo: FileInfo) = withContext(ioDispatcher){
        fileInfoDao.delete(fileInfo)
    }

    override suspend fun delete(url: String): Int = withContext(ioDispatcher){
        return@withContext fileInfoDao.delete( url )
    }

    override suspend fun delete(id: Int): Int = withContext(ioDispatcher){
        return@withContext fileInfoDao.delete(id)
    }

    override suspend fun deleteCompletedFileData(): Int = withContext(ioDispatcher){
        return@withContext fileInfoDao.deleteCompletedFileData()
    }

    override suspend fun deleteAll() = withContext(ioDispatcher){
        fileInfoDao.deleteAll()
    }
}