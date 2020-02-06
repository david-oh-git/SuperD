package io.audioshinigami.superd

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.tonyodev.fetch2.Fetch
import io.audioshinigami.superd.common.SETTINGS_PREF_NAME
import io.audioshinigami.superd.common.THEME_PREF_KEY
import io.audioshinigami.superd.data.source.db.FileDatabase
import io.audioshinigami.superd.zdata.source.FileInfoRepository
import io.audioshinigami.superd.zdata.source.SharedPreferenceRepo
import io.audioshinigami.superd.zdata.source.State
import io.audioshinigami.superd.zdata.source.State.DOWNLOADING
import io.audioshinigami.superd.zdata.source.remote.ActiveListener
import kotlinx.coroutines.runBlocking
import timber.log.Timber

/**
* An application that sets up Timber
 *
 */

class App : Application() , ActiveListener {

    /* database instance */
    var dataBaseInstance: FileDatabase? = null
        get() = field ?: provideDb()
    private set

    /* repository instance*/
    val fileInfoRepository: FileInfoRepository
        get() = ServiceLocator.provideFileInfoRepository(this)

    val sharedPreferenceRepo: SharedPreferenceRepo
        get() = ServiceLocator.provideSharedPreferenceRepository(SETTINGS_PREF_NAME, this)

    /* fetch instance : 3rd party download library*/
    val fetch: Fetch
        get() = ServiceLocator.provideFetch(this)

    /* list of active downloads request ids */
    val activeDownloads: MutableMap<Int, State> = ServiceLocator.provideActiveDownloadsMap()

    override fun onCreate() {
        super.onCreate()

        if( BuildConfig.DEBUG )
            Timber.plant( Timber.DebugTree() )

        synchronized(this){
            instance = this
        }

//        AppCompatDelegate.setDefaultNightMode(
//            AppCompatDelegate.MODE_NIGHT_YES
//        )

    }

    /* provides a db instance*/
    private fun provideDb(): FileDatabase = FileDatabase.getDbInstance(this)

    override fun add(id: Int, isActive: State) {
        activeDownloads[id] = isActive
    }

    override fun isDownloading() = DOWNLOADING in activeDownloads.values

    companion object{
        lateinit var  instance: App
        private set

    }

}