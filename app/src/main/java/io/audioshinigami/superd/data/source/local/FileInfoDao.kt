/*
 * MIT License
 *
 * Copyright (c) 2020  David Osemwota
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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