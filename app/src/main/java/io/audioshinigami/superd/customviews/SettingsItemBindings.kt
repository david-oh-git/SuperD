package io.audioshinigami.superd.customviews

import androidx.databinding.BindingAdapter

/*
* binding for SettingsItem View
* sets the sub title
* */

object SettingsItemBindings {

    @BindingAdapter("app:setting_sub_title")
    @JvmStatic fun funSetSubTitle(item: SettingsItem, theme: String){
        with(item){
            settingSubTitle = theme
        }
    }
}