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

    internal fun provideSharedPreferenceRepository( name: String, context: Context): SharedPreferenceRepo {
        synchronized(this){
            return preferenceRepository ?: createPreferenceRepository(name, context)
        }
    }

    internal fun provideActiveDownloadsMap(): MutableMap<Int, State> {
        synchronized(this){
            return activeDownloads ?: mutableMapOf()
        }
    }

    private fun createPreferenceRepository( name: String ,context: Context): SharedPreferenceRepo {
        return DefaultPreferenceRepository(
            createLocalPreferenceSource(name, context )
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

    @VisibleForTesting
    fun resetRepository(){

        database = null
        fileInfoRepository = null
    }

}