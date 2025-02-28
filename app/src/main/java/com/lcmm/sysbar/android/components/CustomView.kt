package com.lcmm.sysbar.android.components

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.lcmm.sysbar.android.R

/**
 * TODO: document your custom view class.
 */
class CustomView@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {


    init {
        // Inflate the custom layout
        LayoutInflater.from(context).inflate(R.layout.view_round, this, true)

        // Optionally handle attributes
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.RoundView)
        val opacityValue = attributes.getString(R.styleable.RoundView_opacity)
        attributes.recycle()

        setupTransparency(opacityValue?.toInt())
    }

    /**
     *
     */
    private fun setupTransparency(value: Int?){
        if ( value == null || value<1 || value>100){
            return
        }
        // Get the color from resources
        val color = this.backgroundTintList?.defaultColor ?: Color.TRANSPARENT
        val alpha = (255/100)*value
        val transparentColor = Color.argb(alpha, Color.red(color), Color.green(color), Color.blue(color))
        this.backgroundTintList = ColorStateList.valueOf(transparentColor)
    }

}