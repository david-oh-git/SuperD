package io.audioshinigami.superd

import android.content.Context
import android.content.SharedPreferences

object ServiceLocator {

    @Volatile // could be used from multiple threads
    var sharedPreferences: SharedPreferences? = null

    private val lock = Any()

    fun provideSharedPreference( name: String,  context: Context ): SharedPreferences {

        synchronized(this){
            return sharedPreferences ?: createSharedPreference( name, context )
        }
    }

    private fun createSharedPreference( name: String, context: Context ): SharedPreferences {
        return context.getSharedPreferences( name , 0 )
    }
}