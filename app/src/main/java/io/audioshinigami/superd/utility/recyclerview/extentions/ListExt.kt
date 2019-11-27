package io.audioshinigami.superd.utility.recyclerview.extentions

import io.audioshinigami.superd.data.source.db.entity.FileData


fun List<FileData>.updateProgressValue( _progressValue: Int , _url: String ): List<FileData> {
    return map {
        if ( it.url == _url ) it.copy( progressValue = _progressValue ) else it
    }
}
