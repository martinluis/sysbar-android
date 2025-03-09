package com.lcmm.sysbar.android.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import com.lcmm.sysbar.android.R


class SummaryOrderView@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var onConfirmClickListener: OnConfirmClickListener? = null

    /**
     *
     */
    init {
        LayoutInflater.from(context).inflate(R.layout.summary_order_view, this, true)
        val args = context.obtainStyledAttributes(attrs, R.styleable.SummaryOrderView, defStyleAttr, 0)
        args.recycle()
        initView()
    }

    /**
     *
     */
    private fun initView(){
        val confirmButton = findViewById<Button>(R.id.confirmButton)
        val cancelButton = findViewById<Button>(R.id.cancelButton)
        confirmButton.setOnClickListener {
            // Notify the parent when the button is clicked
            onConfirmClickListener?.onConfirmButtonClick()
        }

        cancelButton.setOnClickListener {
            // Notify the parent when the button is clicked
            onConfirmClickListener?.onCancelButtonClick()
        }
    }

    // Method to set the callback listener
    fun setOnConfirmClickListener(listener: OnConfirmClickListener) {
        this.onConfirmClickListener = listener
    }

}

//
interface OnConfirmClickListener {
    fun onConfirmButtonClick()
    fun onCancelButtonClick()
}