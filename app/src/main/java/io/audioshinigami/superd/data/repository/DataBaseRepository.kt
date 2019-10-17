package io.audioshinigami.superd.data.repository

import androidx.lifecycle.LiveData
import io.audioshinigami.superd.data.db.entity.FileData

interface DataBaseRepository {

    suspend fun save(fileData: FileData)

    suspend fun saveAll(vararg fileData: FileData)

    suspend fun update(fileData: FileData)

    suspend fun delete(fileData: FileData)

    suspend fun clearCompleted(): Int

    suspend fun getById(id: Int ): FileData?

    suspend fun deleteById(id: Int): Int

    suspend fun deleteAll()

    suspend fun getAll(): LiveData<List<FileData>>
}