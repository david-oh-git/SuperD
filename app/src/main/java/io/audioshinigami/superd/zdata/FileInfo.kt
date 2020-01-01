package io.audioshinigami.superd.zdata


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import io.audioshinigami.superd.common.TABLE_NAME


@Entity(tableName = TABLE_NAME, indices = arrayOf( Index(value = ["url"], unique = true)))
data class FileInfo(
    @PrimaryKey(autoGenerate = true) val uid: Int,
    @ColumnInfo(name = "request_id") val request_id: Int,
    @ColumnInfo(name = "url") val url: String,
    @ColumnInfo(name = "file_name") val fileName: String,
    @ColumnInfo(name = "progress_value") val progressValue: Int
) {

    val isCompleted: Boolean
    get() = progressValue == 100

    val isEmpty: Boolean
    get() = url.isEmpty() || fileName.isEmpty()

    val isActive: Boolean
    get() = false

} //end FileData