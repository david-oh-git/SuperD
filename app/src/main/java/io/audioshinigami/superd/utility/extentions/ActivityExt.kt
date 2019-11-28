package io.audioshinigami.superd.utility.extentions

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import io.audioshinigami.superd.App


val AppCompatActivity.app : Application
    get() = App.instance