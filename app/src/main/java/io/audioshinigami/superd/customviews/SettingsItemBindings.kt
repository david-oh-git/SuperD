package io.audioshinigami.superd.customviews

import androidx.databinding.BindingAdapter

object SettingsItemBindings {

    @BindingAdapter("app:setting_sub_title")
    @JvmStatic fun funSetSubTitle(item: SettingsItem, theme: String){
        with(item){
            this.settingSubTitle = theme
        }
    }
}