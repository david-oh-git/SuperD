package io.audioshinigami.superd.utility

import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat

object PermissionManager {

    fun requestPermission( context: Context, permission: String , makeRequest: ( String ) -> Unit ){

        if ( isPermissionGranted(context, permission ) )
            return

        if ( canRequestRuntimePermission() ){

            if ( !isPermissionGranted(context, permission ) ){
                createRequestAlert(context, permission, makeRequest )
            }

        } //END if

        return
    }

    /* check if android version supports runtime permission request */
    private fun canRequestRuntimePermission() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M

    fun isPermissionGranted(context: Context, permission: String ): Boolean {

        return ContextCompat.checkSelfPermission( context , permission ) ==
                PackageManager.PERMISSION_GRANTED
    }

    /* creates AlertDialog & requests for permission*/
    private fun createRequestAlert( context: Context, permission: String,
                                    makeRequest: ( String ) -> Unit ){

        val builder = AlertDialog.Builder(context)
        builder.apply {
            setMessage("Write to storage permission is required to download files.")
            setTitle("Permission required.")
            setPositiveButton("Confirm"){ _,_ ->
                makeRequest.invoke(permission)
            }
            setNegativeButton("Reject"){
                dialog, _ ->
                dialog.cancel()
            }
        }

        builder.create().show()
    }


}