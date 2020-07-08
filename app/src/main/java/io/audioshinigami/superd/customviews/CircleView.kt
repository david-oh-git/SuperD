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
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import io.audioshinigami.superd.R
import kotlinx.android.synthetic.main.circle_view.view.*
import kotlin.math.min

/*
* Custom class for a circular view
* */

class CircleView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : RelativeLayout(context, attrs, defStyle) {

    companion object{
        private const val DEFAULT_BACKGROUND_COLOR = Color.TRANSPARENT
        private const val DEFAULT_OVAL_MODE = false
    }

    //if init
    private var initState = false

    var circleColor = DEFAULT_BACKGROUND_COLOR
        set(value) {
            field = value
            if (initState){
                // change the background color
                (container.background.current as GradientDrawable)
                    .apply {
                        setColor(circleColor)

                    }
            }
        }
    // View size in pixels
    private var size = 150
    // ovalMode , false makes the view a square
    var ovalMode = DEFAULT_OVAL_MODE
        set(value) {
            field = value
            invalidate()
            requestLayout()
        }

    var image: Int = 0
        set(value) {
            if (initState){
                (image_id as ImageView)
                    .apply {
                        setImageResource(value)
                    }
            }
        }



    init {

        View.inflate(context, R.layout.circle_view, this)
//        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)
//                as LayoutInflater
//        inflater.inflate(R.layout.circle_view, this)

        val imageView = image_id as ImageView
        val container = container as RelativeLayout

        context.theme.obtainStyledAttributes( attrs ,R.styleable.CircleView, 0,0)
            .apply {

                try {
                    circleColor = getColor(R.styleable.CircleView_circleColor, DEFAULT_BACKGROUND_COLOR)
                    ovalMode = getBoolean(R.styleable.CircleView_ovalMode, DEFAULT_OVAL_MODE)
                    val image = getDrawable(R.styleable.CircleView_image)

                    imageView.setImageDrawable(image)

                    initState = true
                }
                finally {
                    recycle()
                }
            }

        // change the background color
        (container.background.current as GradientDrawable)
            .apply {
                setColor(circleColor)

            }


    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        if(!ovalMode){
            // make parent a square
            size = min(measuredHeight, measuredWidth)
            setMeasuredDimension(size, size)
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)

        if(!ovalMode ){
            // make children fit into resized(square) parent
            // set size of inflated/child layout
            val container = container as RelativeLayout
            container.layout(0,0,size,size)

            val imageView = container.image_id as ImageView
            val halfQuarterSize = (size/4) /2
            // make the image 3/4 of the parent
            imageView.layout(halfQuarterSize,halfQuarterSize,(size * 3/4) + halfQuarterSize,(size * 3/4) + halfQuarterSize)


        }
    }

}