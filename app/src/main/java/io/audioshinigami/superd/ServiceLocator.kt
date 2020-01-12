package io.audioshinigami.superd

import android.content.Context
import android.content.SharedPreferences
import androidx.annotation.VisibleForTesting
import androidx.room.Room
import com.tonyodev.fetch2.Fetch
import com.tonyodev.fetch2.FetchConfiguration
import io.audioshinigami.superd.common.NUMBER_OF_DOWNLOADS_KEY
import io.audioshinigami.superd.common.SETTINGS_PREF_NAME
import io.audioshinigami.superd.zdata.source.DefaultFileInfoRepository
import io.audioshinigami.superd.zdata.source.DownloadDataSource
import io.audioshinigami.superd.zdata.source.FileInfoRepository
import io.audioshinigami.superd.zdata.source.FileInfoSource
import io.audioshinigami.superd.zdata.source.local.FileDatabase
import io.audioshinigami.superd.zdata.source.local.LocalFileInfoSource
import io.audioshinigami.superd.zdata.source.remote.RemoteDownloadDataSource

object ServiceLocator {

    @Volatile // could be used from multiple threads
    var sharedPreferences: SharedPreferences? = null
    private var database: FileDatabase? = null
    private var fetch: Fetch? = null
    @Volatile
    var fileInfoRepository: FileInfoRepository? = null
        @VisibleForTesting set

    private val lock = Any()

    internal fun provideFileInfoRepository( context: Context): FileInfoRepository {
        synchronized(this){
            return fileInfoRepository ?: createFileInfoRepository( context )
        }
    }

    internal fun provideSharedPreference( name: String,  context: Context ): SharedPreferences {

        synchronized(this){
            return sharedPreferences ?: createSharedPreference( name, context )
        }
    }

    private fun createFileInfoRepository( context: Context ): FileInfoRepository {
        val newRepo = DefaultFileInfoRepository( createFileInfoSource(context),
            createDownloadDataSource(context))

        fileInfoRepository = newRepo
        return newRepo
    }

    private fun createFileInfoSource( context: Context ): FileInfoSource {
        val database = database ?: createDatabase(context)

        return LocalFileInfoSource(database.fileDataDao())
    }

    private fun createDownloadDataSource( context: Context ): DownloadDataSource {
        val database = database ?: createDatabase(context)

        return RemoteDownloadDataSource( createFetch( context.applicationContext) , database.fileDataDao() )
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

    /* creates fetch instance*/
    private fun createFetch( context: Context ,numberOfDownloads: Int = 3 ): Fetch {
        val fetchConfig = FetchConfiguration.Builder(context.applicationContext )
            .setDownloadConcurrentLimit(numberOfDownloads)
            .build()

        return Fetch.Impl.getInstance(fetchConfig)
    }

    private fun provideNumberOfDownloads( context: Context): Int {
        val numberOfDownloads = provideSharedPreference( SETTINGS_PREF_NAME , context)
            .getInt( NUMBER_OF_DOWNLOADS_KEY , -99)

        return if( numberOfDownloads == -99) 3 else numberOfDownloads
    }

}