package com.lcmm.sysbar.android.components

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import com.lcmm.sysbar.android.R

/**
 * TODO: document your custom view class.
 */
open class CustomView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayoutCompat(context, attrs, defStyleAttr) {

    private var attributes = context.obtainStyledAttributes(attrs, R.styleable.CustomView)
    private var opacityValue = 100
    private var isRoundValue = true
    private var color = context.getColor(R.color.white)
    private var borderColor = context.getColor(R.color.secondary)
    private var borderSize = 1

    init {
        // Inflate the custom layout
        LayoutInflater.from(context).inflate(R.layout.custom_view, this, true)
        opacityValue = attributes.getInt(R.styleable.CustomView_opacity, 100)
        isRoundValue = attributes.getBoolean(R.styleable.CustomView_isRound, true)
        color = attributes.getColor(R.styleable.CustomView_color, context.getColor(R.color.white))
        borderColor = attributes.getColor(R.styleable.CustomView_borderColor, context.getColor(R.color.secondary))
        borderSize = attributes.getInt(R.styleable.CustomView_borderSize, 1)
        attributes.recycle()
        background = ContextCompat.getDrawable(context, R.drawable.container_round)
        setupBackground(color, opacityValue)
        setupRoundCorners(isRoundValue)
        setupBorder(borderColor, borderSize)
    }

    /**
     *
     */
    private fun setupBackground(color: Int, opacity: Int) {
        val alpha = (255 * opacity) / 100
        val finalColor = Color.argb(alpha, Color.red(color), Color.green(color), Color.blue(color))
        val backgroundDrawable = background as GradientDrawable
        backgroundDrawable.setColor(finalColor)
    }

    /**
     *
     */
    private fun setupBorder(color: Int, borderSize: Int) {
        val scale = resources.displayMetrics.density
        val borderSizeAsPixels = (borderSize * scale + 0.5f).toInt()
        val backgroundDrawable = background as GradientDrawable
        backgroundDrawable.setStroke(borderSizeAsPixels, color)
    }

    /**
     *
     */
    private fun setupRoundCorners(isRound: Boolean) {
        // Only validate when FALSE because the round value comes from container_round
        if (!isRound) {
            val backgroundDrawable = background as GradientDrawable
            backgroundDrawable.cornerRadius = 0f
        }
    }

}