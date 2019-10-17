package io.audioshinigami.superd.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.audioshinigami.superd.data.db.dao.FileDataDao
import io.audioshinigami.superd.data.db.entity.FileData
import io.audioshinigami.superd.data.repository.DataBaseRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/*implementation for database repository as data source */


class DefaultRepository(
    private val dao: FileDataDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : DataBaseRepository {

    override suspend fun save(fileData: FileData) {

    }

    override suspend fun saveAll(vararg fileData: FileData) {

    }

    override fun getById(id: Int): LiveData<FileData?> {
        val ans =  FileData(11, 344553, "http://ieueydhhd/hdjdjd.xcv", "hshdud.xcv",24)
        val _ans = MutableLiveData<FileData>()
        _ans.value = ans
        return _ans
    }

    override suspend fun update(fileData: FileData) {
    }

    override suspend fun clearCompleted() {

    }

    override suspend fun delete(fileData: FileData) {
    }

    override suspend fun deleteAll() {

    }

    override suspend fun deleteById(id: Int) {
    }

    override suspend fun getAll(): LiveData<List<FileData>> {

        return MutableLiveData<List<FileData>>()
    }
}