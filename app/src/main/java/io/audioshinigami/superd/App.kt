package io.audioshinigami.superd

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.tonyodev.fetch2.Fetch
import com.tonyodev.fetch2.FetchConfiguration
import io.audioshinigami.superd.data.source.db.FileDatabase
import timber.log.Timber

class App : Application() {

    /* database instance */
    var dataBaseInstance: FileDatabase? = null
        get() = field ?: provideDb()
    private set

    /* fetch instance : 3rd party download library*/
    val fetch: Fetch
    get() = ServiceLocator.provideFetch(this)

    /* list of active urls */
    lateinit var isActive: MutableMap<String, Boolean>

    override fun onCreate() {
        super.onCreate()

        if( BuildConfig.DEBUG)
            Timber.plant( Timber.DebugTree() )

        synchronized(this){
            instance = this
        }

        isActive = mutableMapOf()


    }

    /* provides a db instance*/
    private fun provideDb(): FileDatabase = FileDatabase.getDbInstance(this)

    /* creates fetch instance*/
    private fun provideFetch(numberOfDownloads: Int = 3): Fetch {
        val fetchConfig = FetchConfiguration.Builder(this)
            .setDownloadConcurrentLimit(numberOfDownloads)
            .build()

        return Fetch.Impl.getInstance(fetchConfig)
    }

    companion object{
        lateinit var  instance: App
        private set

    }

}