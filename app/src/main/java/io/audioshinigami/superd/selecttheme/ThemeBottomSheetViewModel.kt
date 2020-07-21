/*
 * MIT License
 *
 * Copyright (c) 2020  David Osemwota
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.audioshinigami.superd.selecttheme

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.audioshinigami.superd.common.THEME_PREF_KEY
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class ThemeBottomSheetViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

    private val _theme = MutableLiveData<Int>().apply {
        value = sharedPreferences.getInt(THEME_PREF_KEY, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
    }
    val theme: LiveData<Int> = _theme

    val closeFragment: MutableLiveData<Boolean> = MutableLiveData(false)

    fun radioButtonAction(theme: String) {

        viewModelScope.launch(Dispatchers.IO) {
            // assign theme value
            when(theme){
                "dark" -> _theme.postValue( AppCompatDelegate.MODE_NIGHT_YES )
                "light" -> _theme.postValue( AppCompatDelegate.MODE_NIGHT_NO)
                else -> _theme.postValue( AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM )
            }
        }
    }

    fun setTheme(){

        viewModelScope.launch {

            val theme = _theme.value ?: AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM

            // save theme value to preference
            sharedPreferences.edit {
                putInt(THEME_PREF_KEY, theme)
            }

            // set theme
            AppCompatDelegate.setDefaultNightMode(
                theme
            )

            // to close the fragment
            closeFragment.value = true

        }

    }

    fun getCurrentTheme(): Int = sharedPreferences.getInt(THEME_PREF_KEY, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)

    private fun log(message: String){
        Timber.d(message)
    }

}
