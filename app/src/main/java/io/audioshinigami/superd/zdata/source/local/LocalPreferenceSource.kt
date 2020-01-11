package io.audioshinigami.superd.zdata.source.local

import android.content.SharedPreferences
import androidx.core.content.edit
import io.audioshinigami.superd.zdata.source.SharedPreferencesSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LocalPreferenceSource(
    private val sharedPreferences: SharedPreferences ,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): SharedPreferencesSource {

    override suspend fun save(key: String, value: String) =  withContext(ioDispatcher){
        sharedPreferences.edit {
            putString( key, value )
        }
    }

    override suspend fun save(key: String, value: Boolean) = withContext(ioDispatcher) {
        sharedPreferences.edit {
            putBoolean( key, value )
        }
    }

    override suspend fun save(key: String, value: Int) = withContext(ioDispatcher) {
        sharedPreferences.edit {
            putInt( key, value )
        }
    }

    override suspend fun getString(key: String): String? = withContext(ioDispatcher){
        return@withContext sharedPreferences.getString(key, null )
    }

    override suspend fun getBoolean(key: String): Boolean = withContext(ioDispatcher){
        return@withContext sharedPreferences.getBoolean(key, false )
    }

    override suspend fun getInt(key: String): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun remove(key: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}