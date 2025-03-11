package com.lcmm.sysbar.android.components

import android.annotation.SuppressLint
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import com.lcmm.sysbar.android.R
import com.lcmm.sysbar.android.models.OrderItem

@SuppressLint("InflateParams")
class EditOrderItemPopup(context: Context, private val orderItem: OrderItem) : PopupWindow(context) {

    private val quantityText: TextView
    private val productText: TextView
    private val commentInput: TextInputLayout

    private val addButton: ImageButton
    private val subtractButton: ImageButton
    private val closeButton: ImageButton
    private val cancelButton: MaterialButton
    private val confirmButton: MaterialButton

    init {
        // Inflate the custom layout for the popup
        contentView = LayoutInflater.from(context).inflate(R.layout.order_item_dialog, null)

        // Set the width and height of the PopupWindow
        width = LinearLayout.LayoutParams.MATCH_PARENT
        height = LinearLayout.LayoutParams.MATCH_PARENT

        // Set the background drawable (optional, can add a rounded background, shadow, etc.)
        isFocusable = true

        quantityText = contentView.findViewById(R.id.quantityText)
        productText = contentView.findViewById(R.id.productText)
        commentInput = contentView.findViewById(R.id.commentInput)
        addButton = contentView.findViewById(R.id.addButton)
        subtractButton = contentView.findViewById(R.id.subtractButton)
        closeButton = contentView.findViewById(R.id.closeButton)
        cancelButton = contentView.findViewById(R.id.cancelButton)
        confirmButton = contentView.findViewById(R.id.confirmButton)

        initView()
        initListener()
    }

    /**
     *
     */
    @SuppressLint("SetTextI18n")
    private fun initView(){
        productText.text = orderItem.productName
        quantityText.text = orderItem.quantity.toString()
        commentInput.editText?.setText(orderItem.comment)
    }

    /**
     *
     */
    @SuppressLint("SetTextI18n")
    private fun initListener() {
        // Set click listener for the close button
        closeButton.setOnClickListener {
            dismiss()
        }

        cancelButton.setOnClickListener {
            dismiss()
        }

        addButton.setOnClickListener {
            orderItem.quantity++
            quantityText.text = orderItem.quantity.toString()
        }

        subtractButton.setOnClickListener {
            orderItem.quantity--
            if (orderItem.quantity < 0) {
                orderItem.quantity = 0
            }
            quantityText.text = orderItem.quantity.toString()
        }
    }

    /**
     * Show
     */
    fun showPopup(anchorView: View) {
        // Display the PopupWindow anchored to a specific view
        showAsDropDown(anchorView, 0, 0)
    }

    /**
     *
     */
    fun getOrderItem(): OrderItem{
        orderItem.comment = commentInput.editText?.text.toString()
        return orderItem
    }

    /**
     *
     */
    fun setOnConfirmClickListener(listener: OnClickListener) {
        confirmButton.setOnClickListener(listener)
    }
}