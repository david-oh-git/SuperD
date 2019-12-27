package io.audioshinigami.superd.data.source

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import io.audioshinigami.superd.zdata.FileData
import io.audioshinigami.superd.zdata.Result
import io.audioshinigami.superd.zdata.Result.Success
import io.audioshinigami.superd.zdata.source.LocalDataSource

class FakeDataSource (
    var dbData: MutableList<FileData>? = mutableListOf()
) : LocalDataSource {

    override fun observeAll(): LiveData<Result<List<FileData>>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAllPaged(): LiveData<PagedList<FileData>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getDataResult(): Result<List<FileData>> {
        dbData?.let { return Success(it) }

        return Result.Error( Exception("No data found !!"))
    }

    override fun observeFileData(id: Int): LiveData<Result<FileData>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun find(id: Int): FileData? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun find(getUrl: String): FileData? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun insert(vararg allFileData: FileData) {
        for( data in allFileData){
            dbData?.add(data)
        }
    }

    override suspend fun update(fileData: FileData) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun update(url: String, progress: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun updateRequestId(url: String, id: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun delete(fileData: FileData) {
        dbData?.removeAll{
            it == fileData
        }

    }

    override suspend fun delete(url: String): Int {
        val numberDeleted = dbData?.filter { it.url == url }?.size ?: 0
        dbData?.removeAll{
            it.url == url
        }

        return numberDeleted
    }

    override suspend fun delete(id: Int): Int {
        val numberDeleted = dbData?.filter { it.request_id == id }?.size ?: 0
        dbData?.removeAll{
            it.request_id == id
        }

        return numberDeleted
    }

    override suspend fun deleteCompletedFileData(): Int {
        val size = dbData?.filter { it.progressValue == 100 }?.size ?: 0
        dbData?.removeAll { it.progressValue == 100 }

        return size
    }

    override suspend fun deleteAll() {
        dbData?.clear()
    }
}