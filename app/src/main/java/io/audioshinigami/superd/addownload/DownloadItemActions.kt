package io.audioshinigami.superd.addownload

import android.view.View

/*
*  callback from recyclerView item
*/

interface DownloadItemActions {

    fun downloadButtonAction( id: Int, url: String )
    fun showPopup( view: View , itemUrl: String )
    // TODO : add action to check if url is active hence set imageView icon
}