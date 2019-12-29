package io.audioshinigami.superd.zdata.source

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import io.audioshinigami.superd.zdata.FileData
import io.audioshinigami.superd.zdata.Result

interface FileDataRepository {

    fun observeAll(): LiveData<Result<List<FileData>>>

    fun getAllPaged(): LiveData<PagedList<FileData>>

    suspend fun saveFileData( vararg fileData: FileData )

    suspend fun update( fileData: FileData )

    suspend fun update( url: String , progressValue: Int )

    suspend fun find( id: Int ) : FileData?

    suspend fun clearCompleted(): Int

    suspend fun delete( fileData: FileData)

    suspend fun delete( url: String )

    suspend fun delete( id: Int )

    suspend fun deleteAll()

    suspend fun start(url: String, downloadUri: Uri)

    suspend fun restart(url: String, downloadUri: Uri)

    fun pause(id: Int)

    fun resume(id: Int)
}