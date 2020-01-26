package io.audioshinigami.superd.zdata.source

/*
* Download state for downloads
* */

enum class State(val value: Int) {

    /* it is currently downloading*/
    DOWNLOADING(1),

    /* download is paused*/
    PAUSED(2),

    /*something is wrong, can not download*/
    ERROR(3),

    /*download is complete*/
    COMPLETE(4),

    /* default when app is launched*/
    NONE(5);

    companion object{


        fun valueOf(value: Int): State {

            return when(value){
                1 -> DOWNLOADING
                2 -> PAUSED
                3 -> ERROR
                4 -> COMPLETE
                else -> NONE
            }
        }
    }

}