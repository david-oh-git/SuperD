package io.audioshinigami.superd.zdata.source.local


import androidx.room.Database
import androidx.room.RoomDatabase
import io.audioshinigami.superd.data.source.db.dao.FileDataDao
import io.audioshinigami.superd.data.source.db.entity.FileData

/* Room DB that contains each [FileData]*/

@Database(entities = [FileData::class], version = 1 )
abstract class FileDatabase : RoomDatabase(){

    abstract fun fileDataDao(): FileDataDao

} //end Filedatabase