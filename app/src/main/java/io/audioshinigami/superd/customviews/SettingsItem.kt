package io.audioshinigami.superd.customviews

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import io.audioshinigami.superd.R
import kotlinx.android.synthetic.main.settings_item.view.*

class SettingsItem @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
): ConstraintLayout( context, attrs, defStyle) {


    // View size in pixels
    private var size = 300


    init {
        View.inflate(context, R.layout.settings_item, this)

        settings_item_container as ConstraintLayout

        val circleView = settings_item_container.circle_view as CircleView

        circleView.image = R.drawable.ic_download

    }

}