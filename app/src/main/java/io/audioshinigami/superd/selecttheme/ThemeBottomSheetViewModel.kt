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

import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.*
import io.audioshinigami.superd.common.DEFAULT_PREF_INT_VALUE
import io.audioshinigami.superd.common.FOLLOW_SYSTEM
import io.audioshinigami.superd.common.THEME_PREF_KEY
import io.audioshinigami.superd.data.source.SharedPreferenceRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ThemeBottomSheetViewModel(
    private val repository: SharedPreferenceRepo
) : ViewModel() {

    private val _theme: MutableLiveData<Int> = MutableLiveData( FOLLOW_SYSTEM )
    val theme: LiveData<Int> = _theme

    val closeFragment: MutableLiveData<Boolean> = MutableLiveData(false)

    init {
        loadThemeValue()
    }

    private fun loadThemeValue() = viewModelScope.launch (Dispatchers.IO) {
        // get theme value from sharedPreferences & assign
        val themeValue = repository.getInt(THEME_PREF_KEY)

        val theme = if( themeValue == DEFAULT_PREF_INT_VALUE || themeValue == null ) return@launch else themeValue

        _theme.postValue(theme)

    }

    fun radioButtonAction(theme: Int) {

        viewModelScope.launch(Dispatchers.IO) {

            // assign theme value
            _theme.postValue(theme)
        }
    }

    fun setTheme(){

        viewModelScope.launch {

            val theme = _theme.value ?: FOLLOW_SYSTEM

            // save theme value to preference
            repository.save(THEME_PREF_KEY, theme)

            // set theme
            AppCompatDelegate.setDefaultNightMode(
                theme
            )

            // to close the fragment
            closeFragment.value = true

        }
    }

}

@Suppress("UNCHECKED_CAST")
class ThemeBottomSheetViewModelFactory(
    private val repository: SharedPreferenceRepo
): ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        ( ThemeBottomSheetViewModel(repository) as T)
}
