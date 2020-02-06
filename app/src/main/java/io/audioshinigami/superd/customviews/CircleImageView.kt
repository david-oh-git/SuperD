package io.audioshinigami.superd.customviews

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import io.audioshinigami.superd.R

/**
 * TODO: document your custom view class.
 */
class CircleImageView : AppCompatImageView {

    private var _exampleString: String? = null // TODO: use a default from R.string...
    private var _exampleColor: Int = Color.RED // TODO: use a default from R.color...
    private var _exampleDimension: Float = 0f // TODO: use a default from R.dimen...

    /**
     * The text to draw
     */
    var exampleString: String?
        get() = _exampleString
        set(value) {
            _exampleString = value
        }

    /**
     * The font color
     */
    var exampleColor: Int
        get() = _exampleColor
        set(value) {
            _exampleColor = value
        }

    /**
     * In the example view, this dimension is the font size.
     */
    var exampleDimension: Float
        get() = _exampleDimension
        set(value) {
            _exampleDimension = value
        }

    /**
     * In the example view, this drawable is drawn above the text.
     */
    var exampleDrawable: Drawable? = null

    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        // Load attributes
        val attributes = context.obtainStyledAttributes(
            attrs, R.styleable.CircleImageView, defStyle, 0
        )

        _exampleString = attributes.getString(
            R.styleable.CircleImageView_exampleString
        )
        _exampleColor = attributes.getColor(
            R.styleable.CircleImageView_exampleColor,
            exampleColor
        )

        val backgroundColor = attributes.getColor(
            R.styleable.CircleImageView_backgroundColor,
            Color.WHITE
        )
        // Use getDimensionPixelSize or getDimensionPixelOffset when dealing with
        // values that should fall on pixel boundaries.
        _exampleDimension = attributes.getDimension(
            R.styleable.CircleImageView_exampleDimension,
            exampleDimension
        )

        if (attributes.hasValue(R.styleable.CircleImageView_exampleDrawable)) {
            exampleDrawable = attributes.getDrawable(
                R.styleable.CircleImageView_exampleDrawable
            )
            exampleDrawable?.callback = this
        }

        attributes.recycle()

        setColorAndBackground(backgroundColor)
    }

    fun setColorAndBackground(color: Int){
        setBackgroundResource(R.drawable.circle)
        setBackgroundColor(color)

        requestLayout()
        invalidate()
    }


}
