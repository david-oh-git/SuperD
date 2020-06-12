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

package io.audioshinigami.superd.settings

import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.*
import io.audioshinigami.superd.common.DEFAULT_PREF_INT_VALUE
import io.audioshinigami.superd.common.THEME_PREF_KEY
import io.audioshinigami.superd.data.source.SharedPreferenceRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val repository: SharedPreferenceRepo
) : ViewModel() {

    private val _theme: MutableLiveData<String> = MutableLiveData( "" )
    val theme: LiveData<String> = _theme

    init {
        loadThemeValue()
    }

    fun loadThemeValue() = viewModelScope.launch (Dispatchers.IO) {
        // get theme value from sharedPreferences & assign
        val value = repository.getInt(THEME_PREF_KEY)

        val theme = if( value == DEFAULT_PREF_INT_VALUE || value == null ) return@launch else value

        _theme.postValue( getThemeName(theme))
    }

    private fun getThemeName(theme: Int)=
        when(theme){
            AppCompatDelegate.MODE_NIGHT_YES -> "Dark"
            AppCompatDelegate.MODE_NIGHT_NO -> "Light"
            else -> "System Default"
        }
}

@Suppress("UNCHECKED_CAST")
class SettingsViewModelFactory(
    private val repository: SharedPreferenceRepo
): ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        ( SettingsViewModel(repository) as T)
}