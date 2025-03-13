package com.lcmm.sysbar.android.utils

import android.content.Context

import android.widget.Button
import com.lcmm.sysbar.android.R

class ViewUtils { companion object {

    /**
     *
     */
    fun toggleButton(context: Context, button: Button, isEnable: Boolean, color: Int) {
        button.isEnabled = isEnable
        if (isEnable) {
            button.setBackgroundColor(context.getColor(color))
            button.setTextColor(context.getColor(R.color.white))
        }
        else {
            button.setBackgroundColor(context.getColor(R.color.disable))
            button.setTextColor(context.getColor(R.color.black))
        }
    }

} }