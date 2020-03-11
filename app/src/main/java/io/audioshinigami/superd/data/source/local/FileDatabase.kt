package io.audioshinigami.superd.data.source.local



import androidx.room.Database
import androidx.room.RoomDatabase
import io.audioshinigami.superd.data.FileInfo

/* Room DB that contains each [FileData]*/

@Database(entities = [FileInfo::class], version = 1 )
abstract class FileDatabase : RoomDatabase(){

    abstract fun fileDataDao(): FileInfoDao

} //end Filedatabase