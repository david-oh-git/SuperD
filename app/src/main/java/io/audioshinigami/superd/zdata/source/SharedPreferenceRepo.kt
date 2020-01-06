package io.audioshinigami.superd.zdata.source

interface SharedPreferenceRepo {

    suspend fun save( key: String , value: String )

    suspend fun save( key: String , value: Boolean )

    suspend fun save( key: String , value: Int )

    suspend fun getString( key: String ) : String

    suspend fun getBoolean( key: String ) : Boolean

    suspend fun getInt( key: String ) : Int

    suspend fun remove( key: String )

}