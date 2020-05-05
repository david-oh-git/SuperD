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
* An application that
 * - sets up Timber
 * - sets up Dagger
 *
 */

class App : Application() , ActiveListener {

    val sharedPreferenceRepo: SharedPreferenceRepo
        get() = ServiceLocator.provideSharedPreferenceRepository(SETTINGS_PREF_NAME, this)

    /* list of active downloads request ids */
    val activeDownloads: MutableMap<Int, State> = ServiceLocator.provideActiveDownloadsMap()

    // dagger app component
    val appComponent: AppComponent by lazy {
        initDagger()
    }

    override fun onCreate() {
        super.onCreate()

        if( BuildConfig.DEBUG )
            Timber.plant( Timber.DebugTree() )

        synchronized(this){
            instance = this
        }

    }

    override fun add(id: Int, isActive: State) {
        activeDownloads[id] = isActive
    }

    override fun isDownloading() = DOWNLOADING in activeDownloads.values

    open fun initDagger(): AppComponent = DaggerAppComponent.factory().create(this)

    companion object{
        lateinit var  instance: App
        private set

    }

}