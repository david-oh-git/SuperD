package io.audioshinigami.superd.utility.extentions

import android.content.Context
import android.widget.Toast

fun Context.toast(message: String ): Toast = Toast.makeText(this, message, Toast.LENGTH_LONG)
    .apply { show() }

