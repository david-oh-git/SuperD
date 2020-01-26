package io.audioshinigami.superd.zdata.source.remote

import io.audioshinigami.superd.zdata.source.State

/*
* An interface for adding active ids to activeDownloads Map on [io.audioshinigami.superd.App]*/

interface ActiveListener {

    fun add(id: Int, isActive: State)

    fun isDownloading(): Boolean
}