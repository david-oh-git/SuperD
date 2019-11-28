package io.audioshinigami.superd.data.repository

import androidx.lifecycle.LiveData
import io.audioshinigami.superd.data.source.db.entity.FileData

/*
* Repository for Room Db
*/

interface DataBaseRepository {

    suspend fun save(fileData: FileData)

    suspend fun save(vararg fileData: FileData)

    suspend fun update(url: String, progressValue: Int )

    suspend fun update(fileData: FileData)

    suspend fun delete(fileData: FileData)

    suspend fun clearCompleted(): Int

    suspend fun getById(id: Int ): FileData?

    suspend fun delete(id: Int): Int

    suspend fun delete( url: String ): Int

    suspend fun deleteAll()

    suspend fun getAll(): LiveData<List<FileData>>

    suspend fun getData(): List<FileData>

}