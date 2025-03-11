package com.lcmm.sysbar.android.components

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.constraintlayout.widget.ConstraintLayout
import com.lcmm.sysbar.android.R
import com.lcmm.sysbar.android.models.OrderItem
import com.lcmm.sysbar.android.utils.StringUtils


class OrderItemView@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayoutCompat(context, attrs, defStyleAttr) {

    private val quantityText: TextView
    private val nameText: TextView
    private val totalText: TextView
    private val commentText: TextView

    private val addButton: ImageButton
    private val subtractButton: ImageButton
    private val deleteButton: ImageButton
    private val editButton: ImageButton

    private lateinit var orderItem: OrderItem

    /**
     *
     */
    init {
        LayoutInflater.from(context).inflate(R.layout.order_item_view, this, true)

        quantityText = findViewById(R.id.quantityText)
        nameText = findViewById(R.id.productText)
        totalText = findViewById(R.id.totalText)
        commentText = findViewById(R.id.commentText)
        addButton = findViewById(R.id.addButton)
        subtractButton = findViewById(R.id.subtractButton)
        deleteButton = findViewById(R.id.deleteButton)
        editButton = findViewById(R.id.editButton)

        val args = context.obtainStyledAttributes(attrs, R.styleable.OrderItemView, defStyleAttr, 0)
        args.recycle()

        initView()
    }

    /**
     *
     */
    @SuppressLint("SetTextI18n")
    private fun initView() {
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

        editButton.setOnClickListener {
            showEditPopup()
        }
    }

    /**
     *
     */
    @SuppressLint("SetTextI18n")
    fun bindData(orderItem: OrderItem){
        this.orderItem = orderItem
        quantityText.text = orderItem.quantity.toString()
        nameText.text = orderItem.productName
        totalText.text = StringUtils.decimalToCurrencyFormat( orderItem.getTotal() )
        commentText.text = orderItem.comment
        commentText.visibility = if (orderItem.comment.isEmpty()) View.GONE else View.VISIBLE
        if (orderItem.itemId != null) {
            enableReadOnly()
        }
    }

    /**
     *
     */
    private fun enableReadOnly() {
        val actionsContainer = findViewById<ConstraintLayout>(R.id.actionsContainer)
        actionsContainer.visibility = View.GONE
    }

    /**
     *
     */
    fun setDeleteClickListener(listener: OnClickListener) {
        deleteButton.setOnClickListener(listener)
    }

    /**
     *
     */
    @SuppressLint("InflateParams")
    private fun showEditPopup() {
        // Create an instance of your custom PopupWindow
        val customPopup = EditOrderItemPopup(context, orderItem)

        customPopup.setOnConfirmClickListener {
            bindData(customPopup.getOrderItem())
            customPopup.dismiss()
        }

        // Show the popup
        customPopup.showPopup(editButton)
    }

}