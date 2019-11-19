package io.audioshinigami.superd.addownload

import io.audioshinigami.superd.data.source.db.entity.FileData

/*
* Handles click actions for a download_item xml
*/

class ItemActions(
    private val downloadAction: ( String ) -> Unit,
    private val popUpAction: ( FileData ) -> Unit
)