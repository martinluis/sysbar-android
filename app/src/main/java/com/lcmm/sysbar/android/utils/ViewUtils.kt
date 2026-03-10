package com.lcmm.sysbar.android.utils

import android.content.Context
import android.graphics.Color

import android.widget.Button
import com.lcmm.sysbar.android.R

class ViewUtils { companion object {

    /**
     *
     */
    fun toggleButton(context: Context, button: Button, isEnable: Boolean, color: Int) {
        button.isEnabled = isEnable

        val baseColor = context.getColor(color)

        if (isEnable) {
            // Normal enabled color
            button.setBackgroundColor(baseColor)
            button.setTextColor(context.getColor(R.color.white))
        } else {
            // Lighten the base color toward white for disabled state
            val disabledColorInt = lightenColor(baseColor, 0.4f) // 0.0 = no change, 1.0 = white
            button.setBackgroundColor(disabledColorInt)
            button.setTextColor(context.getColor(R.color.white))
        }
    }


    fun lightenColor(color: Int, factor: Float): Int {
        val red = Color.red(color)
        val green = Color.green(color)
        val blue = Color.blue(color)

        val newRed = red + ((255 - red) * factor).toInt()
        val newGreen = green + ((255 - green) * factor).toInt()
        val newBlue = blue + ((255 - blue) * factor).toInt()

        return Color.argb(Color.alpha(color), newRed, newGreen, newBlue)
    }

} }