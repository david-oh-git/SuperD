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

package io.audioshinigami.superd.customviews

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import io.audioshinigami.superd.R
import kotlinx.android.synthetic.main.circle_view.view.*
import kotlinx.android.synthetic.main.settings_item.view.*

class SettingsItem @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
): ConstraintLayout( context, attrs, defStyle) {

    // TODO work on issue on API 19

    companion object{
        private const val DEFAULT_ICON_BACKGROUND_COLOR = Color.TRANSPARENT
    }
    // View size in pixels
    private var size = 300
    var circleColor = DEFAULT_ICON_BACKGROUND_COLOR

    var settingSubTitle = ""
        set(value) {
            field = value
            settings_item_container.sub_title.text = value
        }

    init {
        View.inflate(context, R.layout.settings_item, this)

        settings_item_container as ConstraintLayout

        context.theme.obtainStyledAttributes(attrs, R.styleable.SettingsItem ,0,0)
            .apply {
                try {
                    circleColor = getColor(R.styleable.SettingsItem_setting_image_color, DEFAULT_ICON_BACKGROUND_COLOR)
                    val image = getDrawable(R.styleable.SettingsItem_setting_image)
                    val title = getString(R.styleable.SettingsItem_setting_title)
                    settingSubTitle = getString(R.styleable.SettingsItem_setting_sub_title) ?: ""

                    val circleView = settings_item_container.circle_view.image_id as ImageView
                    circleView.setImageDrawable(image)

                    settings_item_container.title.text = title
                    settings_item_container.sub_title.text = settingSubTitle

                    settings_item_container.circle_view.circleColor = circleColor
                }
                finally {
                    recycle()
                }
            }



    }

}