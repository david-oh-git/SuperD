package io.audioshinigami.superd

import android.app.Application
import com.tonyodev.fetch2.Fetch
import io.audioshinigami.superd.data.source.db.FileDatabase
import io.audioshinigami.superd.zdata.source.DownloadDataSource
import io.audioshinigami.superd.zdata.source.FileInfoRepository
import io.audioshinigami.superd.zdata.source.remote.ActiveListener
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
    val repository: FileInfoRepository
        get() = ServiceLocator.provideFileInfoRepository(this)

    /* fetch instance : 3rd party download library*/
    val fetch: Fetch
        get() = ServiceLocator.provideFetch(this)

    /* list of active downloads request ids */
    val activeDownloads: MutableMap<Int, Boolean> = ServiceLocator.provideActiveDownloadsMap()

    override fun onCreate() {
        super.onCreate()

        if( BuildConfig.DEBUG)
            Timber.plant( Timber.DebugTree() )

        synchronized(this){
            instance = this
        }

    }

    /* provides a db instance*/
    private fun provideDb(): FileDatabase = FileDatabase.getDbInstance(this)

    override fun add(id: Int, isActive: Boolean) {
        activeDownloads[id] = isActive
    }

    override fun isDownloading() = true in activeDownloads.values

    override fun onError(id: Int) {
        activeDownloads.remove(id)
    }

    companion object{
        lateinit var  instance: App
        private set

    }

}