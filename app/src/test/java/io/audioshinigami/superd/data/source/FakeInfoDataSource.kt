package io.audioshinigami.superd.data.source

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import io.audioshinigami.superd.data.FileInfo
import io.audioshinigami.superd.data.Result
import io.audioshinigami.superd.data.Result.Success

class FakeInfoDataSource (
    var dbData: MutableList<FileInfo>? = mutableListOf()
) : FileInfoSource {

    override fun observeAll(): LiveData<Result<List<FileInfo>>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAllPaged(): LiveData<PagedList<FileInfo>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getDataResult(): Result<List<FileInfo>> {
        dbData?.let { return Success(it) }

        return Result.Error( Exception("No data found !!"))
    }

    override fun observeFileData(id: Int): LiveData<Result<FileInfo>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun find(id: Int): FileInfo? {
        return dbData?.find { it.uid == id }
    }

    override suspend fun find(getUrl: String): FileInfo? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun insert(vararg allFileData: FileInfo) {
        for( data in allFileData){
            dbData?.add(data)
        }
    }

    override suspend fun update(fileInfo: FileInfo) {
        val replace = dbData?.find { it.uid == fileInfo.uid }

        dbData?.indexOf(replace)?.run {

            dbData?.remove( replace )

            dbData?.add( this, fileInfo)
        }

    }

    override suspend fun update(url: String, progress: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun updateRequestId(url: String, id: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun delete(fileInfo: FileInfo) {
        dbData?.removeAll{
            it == fileInfo
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