package io.audioshinigami.superd.di.components

import dagger.Component
import io.audioshinigami.superd.MainActivity
import io.audioshinigami.superd.di.modules.AppModule
import javax.inject.Singleton

@Singleton
@Component( modules = [AppModule::class] )
interface AppComponent {

    fun inject( target: MainActivity )
}