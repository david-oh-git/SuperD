package io.audioshinigami.superd.utility

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import io.audioshinigami.superd.App

fun Context.toast(message: String ): Toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
    .apply { show() }

