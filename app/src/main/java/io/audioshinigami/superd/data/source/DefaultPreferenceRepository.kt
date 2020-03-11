package io.audioshinigami.superd.data.source

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DefaultPreferenceRepository(
    private val sharedPreferences: SharedPreferencesSource ,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): SharedPreferenceRepo {

    override suspend fun save(key: String, value: String) = withContext(ioDispatcher) {
        sharedPreferences.save(key, value)
    }

    override suspend fun save(key: String, value: Boolean) = withContext(ioDispatcher){
        sharedPreferences.save(key, value)
    }

    override suspend fun save(key: String, value: Int) = withContext(ioDispatcher){
        sharedPreferences.save(key, value)
    }

    override suspend fun getString(key: String): String? = withContext(ioDispatcher){
        return@withContext sharedPreferences.getString(key)
    }

    override suspend fun getBoolean(key: String)= withContext(ioDispatcher) {
        sharedPreferences.getBoolean(key)
    }

    override suspend fun getInt(key: String)= withContext(ioDispatcher){
        return@withContext sharedPreferences.getInt(key)
    }

    override suspend fun remove(key: String)= withContext(ioDispatcher) {
        sharedPreferences.remove(key)
    }
}