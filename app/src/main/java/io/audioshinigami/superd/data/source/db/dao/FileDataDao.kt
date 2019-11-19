package io.audioshinigami.superd.data.source.db.dao


import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import io.audioshinigami.superd.common.TABLE_NAME
import io.audioshinigami.superd.data.source.db.entity.FileData

@Dao
interface FileDataDao {
/*
* for some strange issues/probably a bug, getAll cant be a suspend function*/
    @Query("SELECT * FROM $TABLE_NAME ORDER BY uid ASC")
    fun getAll(): LiveData<List<FileData>>

    @Query("SELECT * FROM $TABLE_NAME ORDER BY uid ASC")
    suspend fun getData(): List<FileData>

    @Query("SELECT * FROM $TABLE_NAME ORDER BY uid DESC")
    fun getAllPaged(): DataSource.Factory<Int, FileData>

    // @Query("SELECT * FROM People WHERE id = :id")   
    @Query("SELECT * FROM $TABLE_NAME WHERE url = :getUrl")
    fun findByUrl(getUrl: String): FileData?

    // @Query("SELECT * FROM People WHERE id = :id")
    @Query("SELECT * FROM $TABLE_NAME WHERE uid = :id")
    suspend fun findById(id: Int): FileData?

    @Insert
    suspend fun insertAll(vararg allFileData: FileData)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(fileData: FileData)

    @Update
    suspend fun updateFileData(fileData: FileData)

    // updates progress value for a fileData, as url is unique
    @Query("UPDATE $TABLE_NAME SET progress_value = :progress WHERE url = :url")
    suspend fun updateProgressValue(url: String, progress: Int)

    // updates request id for a fileData, as url is unique
    @Query("UPDATE $TABLE_NAME SET request_id = :id WHERE url = :url")
    suspend fun updateRequestId(url: String, id: Int)

    @Delete
    suspend fun delete(fileData: FileData)

    /* delete FileData with uid. This should return number of FileData deleted. it should be 1*/
    @Query("DELETE FROM $TABLE_NAME WHERE uid = :id")
    suspend fun deleteById(id: Int): Int

    /*
    * deletes all completed data, returns number of deletes items
    * */
    @Query("DELETE FROM $TABLE_NAME WHERE progress_value = 100")
    suspend fun deleteCompletedFileData(): Int

    @Query("DELETE FROM $TABLE_NAME")
    suspend fun deleteAll()
}