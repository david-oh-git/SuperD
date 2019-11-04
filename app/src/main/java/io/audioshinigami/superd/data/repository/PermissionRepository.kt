package io.audioshinigami.superd.data.repository

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

class PermissionRepository( val context: Context ) {

    private val preferenceName = "PREF"
    private val sharedPreferences = context.getSharedPreferences( preferenceName, MODE_PRIVATE)
    private var editor: SharedPreferences.Editor? = null

    private fun initEdit(){
        if ( editor == null )
            editor = sharedPreferences.edit()

    }

    fun firstTimeAsk( permission: String, isFirstTime: Boolean ){
        initEdit()

        editor?.apply {
            putBoolean( permission, isFirstTime )
            commit()
        }
        editor = null
        
    }
}