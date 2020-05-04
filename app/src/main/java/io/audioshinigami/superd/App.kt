package io.audioshinigami.superd

import android.app.Application
import com.tonyodev.fetch2.Fetch
import io.audioshinigami.superd.common.SETTINGS_PREF_NAME
import io.audioshinigami.superd.data.source.FileInfoRepository
import io.audioshinigami.superd.data.source.SharedPreferenceRepo
import io.audioshinigami.superd.data.source.State
import io.audioshinigami.superd.data.source.State.DOWNLOADING
import io.audioshinigami.superd.data.source.remote.ActiveListener
import io.audioshinigami.superd.di.components.AppComponent
import io.audioshinigami.superd.di.components.DaggerAppComponent
import io.audioshinigami.superd.di.modules.AppModule
import timber.log.Timber

/**
* An application that sets up Timber
 *
 */

class App : Application() , ActiveListener {

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

    // dagger app component
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        if( BuildConfig.DEBUG )
            Timber.plant( Timber.DebugTree() )

        appComponent = initDagger(this)

        synchronized(this){
            instance = this
        }

    }

    override fun add(id: Int, isActive: State) {
        activeDownloads[id] = isActive
    }

    override fun isDownloading() = DOWNLOADING in activeDownloads.values

    private fun initDagger(app: App ): AppComponent =
        DaggerAppComponent.builder()
            .appModule( AppModule(app))
            .build()

    companion object{
        lateinit var  instance: App
        private set

    }

}