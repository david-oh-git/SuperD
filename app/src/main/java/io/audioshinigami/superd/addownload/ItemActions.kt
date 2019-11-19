package io.audioshinigami.superd.addownload

import io.audioshinigami.superd.data.source.db.entity.FileData

/*
* Handles click actions for a download_item xml
*/

class ItemActions(
    val downloadAction: ( Int , String ) -> Unit,
    val overFlowAction: (FileData ) -> Unit
)