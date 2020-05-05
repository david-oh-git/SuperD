package io.audioshinigami.superd.di.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import io.audioshinigami.superd.App
import io.audioshinigami.superd.data.source.remote.ActiveListener
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
object AppModule {

    @Singleton
    @Provides
    fun provideApp(appContext: Context ): App = ( appContext as App )

    @JvmStatic
    @Singleton
    @Provides
    fun provideActiveListener(app: App ): ActiveListener = app

    @JvmStatic
    @Singleton
    @Provides
    fun provideIoDispatcher() = Dispatchers.IO
}