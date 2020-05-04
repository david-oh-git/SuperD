package io.audioshinigami.superd

import android.content.Context
import android.content.SharedPreferences
import androidx.annotation.VisibleForTesting
import androidx.room.Room
import com.tonyodev.fetch2.Fetch
import com.tonyodev.fetch2.FetchConfiguration
import io.audioshinigami.superd.common.NUMBER_OF_DOWNLOADS_KEY
import io.audioshinigami.superd.common.SETTINGS_PREF_NAME
import io.audioshinigami.superd.data.source.*
import io.audioshinigami.superd.data.source.local.FileDatabase
import io.audioshinigami.superd.data.source.local.LocalFileInfoSource
import io.audioshinigami.superd.data.source.local.LocalPreferenceSource
import io.audioshinigami.superd.data.source.remote.RemoteDownloadDataSource

object ServiceLocator {

    @Volatile // could be used from multiple threads
    var sharedPreferences: SharedPreferences? = null
    private var database: FileDatabase? = null
    @Volatile
    var fileInfoRepository: FileInfoRepository? = null
        @VisibleForTesting set

    @Volatile
    var preferenceRepository: SharedPreferenceRepo? = null

    @Volatile
    private var activeDownloads: MutableMap<Int, State>? = null

    @Volatile
    var fetch: Fetch? = null

    private val lock = Any()

    internal fun provideFileInfoRepository( context: Context): FileInfoRepository {
        synchronized(this){
            return fileInfoRepository ?: createFileInfoRepository( context )
        }
    }

    internal fun provideSharedPreferenceRepository( name: String, context: Context): SharedPreferenceRepo {
        synchronized(this){
            return preferenceRepository ?: createPreferenceRepository(name, context)
        }
    }

    internal fun provideFetch( context: Context ): Fetch {
        synchronized(this){
            val defaultFetch = fetch
            if( defaultFetch == null || defaultFetch.isClosed ){
                val newFetch = createFetch(context , getNumberOfDownloads(context))
                fetch = newFetch
                return newFetch
            }
            return fetch ?: createFetch(context, getNumberOfDownloads(context))
        }
    }

    private fun getNumberOfDownloads(context: Context): Int {
        val numberOfDownloads = provideSharedPreference( SETTINGS_PREF_NAME , context.applicationContext)
            .getInt( NUMBER_OF_DOWNLOADS_KEY , -99)

        return if( numberOfDownloads == -99) 2 else numberOfDownloads
    }

    internal fun provideSharedPreference( name: String,  context: Context ): SharedPreferences {

        synchronized(this){
            return sharedPreferences ?: createSharedPreference( name, context )
        }
    }

    internal fun provideActiveDownloadsMap(): MutableMap<Int, State> {
        synchronized(this){
            return activeDownloads ?: mutableMapOf()
        }
    }

    private fun createFileInfoRepository( context: Context ): FileInfoRepository {
        val newRepo = DefaultFileInfoRepository( createFileInfoSource(context),
            createDownloadDataSource(context))

        fileInfoRepository = newRepo
        return newRepo
    }

    private fun createPreferenceRepository( name: String ,context: Context): SharedPreferenceRepo {
        return DefaultPreferenceRepository(
            createLocalPreferenceSource(name, context )
        )
    }

    private fun createFileInfoSource( context: Context ): FileInfoSource {
        val database = database ?: createDatabase(context)

        return LocalFileInfoSource(database.fileDataDao())
    }

    private fun createDownloadDataSource( context: Context ): DownloadDataSource {
        val database = database ?: createDatabase(context)

        return RemoteDownloadDataSource(
            provideFetch( context.applicationContext )
            , database.fileDataDao(),
            ( (context.applicationContext as App) )
        )
    }

    private fun createSharedPreference( name: String, context: Context ): SharedPreferences {
        return context.getSharedPreferences( name , 0 )
    }

    private fun createLocalPreferenceSource(name: String, context: Context): LocalPreferenceSource {
        return LocalPreferenceSource(
            createSharedPreference(name, context)
        )
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
    private fun createFetch( context: Context ,numberOfDownloads: Int ): Fetch {
        val fetchConfig = FetchConfiguration.Builder(context.applicationContext )
            .setDownloadConcurrentLimit(numberOfDownloads)
            .enableLogging(true)
            .build()

        val newFetch = Fetch.Impl.getInstance(fetchConfig)
        fetch = newFetch
        return newFetch
    }

    @VisibleForTesting
    fun resetRepository(){

        database = null
        fileInfoRepository = null
    }

}