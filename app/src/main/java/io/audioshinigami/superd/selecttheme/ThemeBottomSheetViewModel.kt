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
