package io.audioshinigami.superd

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import io.audioshinigami.superd.zdata.source.FileInfoSource
import io.audioshinigami.superd.zdata.source.local.FileDatabase
import io.audioshinigami.superd.zdata.source.local.LocalFileInfoSource

object ServiceLocator {

    @Volatile // could be used from multiple threads
    var sharedPreferences: SharedPreferences? = null
    private var database: FileDatabase? = null

    private val lock = Any()

    fun provideSharedPreference( name: String,  context: Context ): SharedPreferences {

        synchronized(this){
            return sharedPreferences ?: createSharedPreference( name, context )
        }
    }
    
    private fun createFileInfoSource( context: Context ): FileInfoSource {
        val database = database ?: createDatabase(context)

        return LocalFileInfoSource(database.fileDataDao())
    }

    private fun createSharedPreference( name: String, context: Context ): SharedPreferences {
        return context.getSharedPreferences( name , 0 )
    }

    private fun createDatabase( context: Context ): FileDatabase {
        val result = Room.databaseBuilder(
            context.applicationContext,
            FileDatabase::class.java, "filedownload_db"
        ).build()
        database = result
        return result
    }
}