package io.audioshinigami.superd.data.repository

import io.audioshinigami.superd.data.db.dao.FileDataDao
import io.audioshinigami.superd.data.db.entity.FileData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/*implementation for database repository as data source */


class DefaultRepository(
    private val dao: FileDataDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : DataBaseRepository {

    override suspend fun save(fileData: FileData) = withContext(ioDispatcher){
        dao.insert(fileData)
    }

    override suspend fun saveAll(vararg fileData: FileData) = withContext(ioDispatcher){
        dao.insertAll(*fileData)
    }

    override suspend fun getById(id: Int): FileData? {
        return dao.findById(id)
    }

    override suspend fun update(fileData: FileData) = withContext(ioDispatcher) {
        dao.updateFileData(fileData)
    }

    override suspend fun clearCompleted() = withContext(ioDispatcher){
        return@withContext dao.deleteCompletedFileData()
    }

    override suspend fun delete(fileData: FileData) = withContext(ioDispatcher){
        dao.delete(fileData)
    }

    override suspend fun deleteAll() = withContext(ioDispatcher){
        dao.deleteAll()
    }

    override suspend fun deleteById(id: Int) = withContext(ioDispatcher) {
        dao.deleteById(id)
    }

    override suspend fun getAll() = dao.getAll()
}