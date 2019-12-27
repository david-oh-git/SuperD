package io.audioshinigami.superd.zdata.source.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import io.audioshinigami.superd.zdata.FileData
import io.audioshinigami.superd.zdata.Result
import io.audioshinigami.superd.zdata.Result.Error
import io.audioshinigami.superd.zdata.Result.Success
import io.audioshinigami.superd.zdata.source.FileDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LocalFileDataSource internal constructor(
    private val fileDataDao: FileDataDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : FileDataSource {

    private val CACHED_PAGE_SIZE = 10

    override fun observeAll(): LiveData<Result<List<FileData>>> {
        return Transformations.map( fileDataDao.observeAll() ){
            Success(it)
        }
    }

    override fun getAllPaged(): LiveData<PagedList<FileData>> {

        return LivePagedListBuilder<Int, FileData>( fileDataDao.getAllPaged() ,
            CACHED_PAGE_SIZE ).build()
    }

    override suspend fun getDataResult(): Result<List<FileData>> = withContext(ioDispatcher){
        return@withContext try {
            Success( fileDataDao.getData() )
        }catch (e: Exception ){
            Error(e)
        }
    }

    override fun observeFileData(id: Int): LiveData<Result<FileData>> {
        return Transformations.map( fileDataDao.observeFileData(id) ){
            Success(it)
        }
    }

    override suspend fun find(id: Int): FileData? = withContext(ioDispatcher){
        return@withContext fileDataDao.find(id)
    }

    override suspend fun find(getUrl: String): FileData? = withContext(ioDispatcher){
        return@withContext fileDataDao.find(getUrl)
    }

    override suspend fun insert(vararg allFileData: FileData) = withContext(ioDispatcher) {
        fileDataDao.insert( *allFileData )
    }

    override suspend fun update(fileData: FileData) = withContext(ioDispatcher){
        fileDataDao.update(fileData)
    }

    override suspend fun update(url: String, progress: Int) = withContext(ioDispatcher){
        fileDataDao.update(url, progress)
    }

    override suspend fun updateRequestId(url: String, id: Int) = withContext(ioDispatcher){
        fileDataDao.updateRequestId(url, id )
    }

    override suspend fun delete(fileData: FileData) = withContext(ioDispatcher){
        fileDataDao.delete(fileData)
    }

    override suspend fun delete(url: String): Int = withContext(ioDispatcher){
        return@withContext fileDataDao.delete( url )
    }

    override suspend fun delete(id: Int): Int = withContext(ioDispatcher){
        return@withContext fileDataDao.delete(id)
    }

    override suspend fun deleteCompletedFileData(): Int = withContext(ioDispatcher){
        return@withContext fileDataDao.deleteCompletedFileData()
    }

    override suspend fun deleteAll() = withContext(ioDispatcher){
        fileDataDao.deleteAll()
    }
}