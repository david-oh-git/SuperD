package io.audioshinigami.superd.settings

import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.*
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

        val theme = if( value == -999 || value == null ) return@launch else value

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