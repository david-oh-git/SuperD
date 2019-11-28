package io.audioshinigami.superd.utility.extentions

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context

fun Context.copyToClipBoard( text: CharSequence ){

    val clipBoard = getSystemService( Context.CLIPBOARD_SERVICE ) as ClipboardManager
    val clip: ClipData =  ClipData.newPlainText("label", text )

    clipBoard.setPrimaryClip(clip)

}