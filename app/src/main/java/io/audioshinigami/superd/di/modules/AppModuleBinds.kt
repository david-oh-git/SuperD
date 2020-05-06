package io.audioshinigami.superd.di.modules

import dagger.Binds
import dagger.Module
import io.audioshinigami.superd.data.source.DefaultRepository
import io.audioshinigami.superd.data.source.FileInfoRepository
import javax.inject.Singleton

@Module
abstract class AppModuleBinds {

    @Singleton
    @Binds
    abstract fun bindRepository(repository: DefaultRepository ): FileInfoRepository
}