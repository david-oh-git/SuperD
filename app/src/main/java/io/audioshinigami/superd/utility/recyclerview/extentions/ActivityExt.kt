package io.audioshinigami.superd.utility.recyclerview.extentions

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import io.audioshinigami.superd.App


val AppCompatActivity.app : Application
    get() = App.instance