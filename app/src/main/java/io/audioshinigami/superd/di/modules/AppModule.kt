package io.audioshinigami.superd.di.modules

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import io.audioshinigami.superd.App
import io.audioshinigami.superd.data.source.remote.ActiveListener
import javax.inject.Singleton

@Module
class AppModule(val app: Application) {

    @Singleton
    @Provides
    fun provideContext(): Context = app.applicationContext

    @Singleton
    @Provides
    fun provideApp(): App = ( app as App )

    @Singleton
    @Provides
    fun provideActiveListener(_app: App ): ActiveListener = _app
}