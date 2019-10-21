package io.audioshinigami.superd.models

/*enum class for download state
* if new or already in DB
* */

enum class DownloadStatus(val type: Int) {
    /* needs a new download request*/
    NEW(0),
    /* already exists & is present in DB but not completed */
    EXISTING(1),
    /* download 100% done*/
    COMPLETE(2),
    /*download is or has been active since app was launched */
    ACTIVE(3)
}