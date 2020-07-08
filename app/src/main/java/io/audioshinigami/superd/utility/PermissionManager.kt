/*
 * MIT License
 *
 * Copyright (c) 2020  David Osemwota
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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