package io.audioshinigami.superd.data.source.local


import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import io.audioshinigami.superd.common.TABLE_NAME
import io.audioshinigami.superd.data.FileInfo

/* data access for DB table, info on each download */

@Dao
interface FileInfoDao {
/*
* for some strange issues/probably a bug, getAll cant be a suspend function*/
    @Query("SELECT * FROM $TABLE_NAME ORDER BY uid ASC")
    fun observeAll(): LiveData<List<FileInfo>>

    // @Query("SELECT * FROM People WHERE id = :id")
    @Query("SELECT * FROM $TABLE_NAME WHERE uid = :id")
    fun observeFileInfo(id: Int): LiveData<FileInfo>

    @Query("SELECT * FROM $TABLE_NAME ORDER BY uid ASC")
    suspend fun getData(): List<FileInfo>

    @Query("SELECT * FROM $TABLE_NAME ORDER BY uid DESC")
    fun getAllPaged(): DataSource.Factory<Int, FileInfo>

    // @Query("SELECT * FROM People WHERE id = :id")   
    @Query("SELECT * FROM $TABLE_NAME WHERE url = :getUrl")
    suspend fun find(getUrl: String): FileInfo?

    // @Query("SELECT * FROM People WHERE id = :id")
    @Query("SELECT * FROM $TABLE_NAME WHERE uid = :id")
    suspend fun find(id: Int): FileInfo?

    @Insert
    suspend fun insert(vararg allFileData: FileInfo)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(fileInfo: FileInfo)

    @Update /* replaces item*/
    suspend fun update(fileInfo: FileInfo)

    // updates progress value for a fileData, as url is unique
    @Query("UPDATE $TABLE_NAME SET progress_value = :progress WHERE url = :url")
    suspend fun update(url: String, progress: Int)

    // updates request id for a fileData, as url is unique
    @Query("UPDATE $TABLE_NAME SET request_id = :id WHERE url = :url")
    suspend fun updateRequestId(url: String, id: Int)

    @Delete
    suspend fun delete(fileInfo: FileInfo)

    /* deletes row with URL*/
    @Query("DELETE FROM $TABLE_NAME WHERE url = :url")
    suspend fun delete( url: String ): Int

    /* delete FileData with uid. This should return number of FileData deleted. it should be 1*/
    @Query("DELETE FROM $TABLE_NAME WHERE uid = :id")
    suspend fun delete(id: Int): Int

    /*
    * deletes all completed data, returns number of deletes items
    * */
    @Query("DELETE FROM $TABLE_NAME WHERE progress_value = 100")
    suspend fun deleteCompletedFileData(): Int

    @Query("DELETE FROM $TABLE_NAME")
    suspend fun deleteAll()
}