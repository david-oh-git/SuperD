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