package com.lcmm.sysbar.android.components

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import com.lcmm.sysbar.android.R
import com.lcmm.sysbar.android.models.OrderItem
import com.lcmm.sysbar.android.utils.StringUtils
import com.lcmm.sysbar.android.viewModel.SummaryOrderViewModel


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

    private val statusBar: View

    private lateinit var orderItem: OrderItem

    private lateinit var viewModel: SummaryOrderViewModel


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
        statusBar = findViewById(R.id.statusBar)

        val args = context.obtainStyledAttributes(attrs, R.styleable.OrderItemView, defStyleAttr, 0)
        args.recycle()

        initView()
        initViewModel()
    }

    /**
     *
     */
    @SuppressLint("SetTextI18n")
    private fun initView() {
        addButton.setOnClickListener {
            orderItem.quantity++
            quantityText.text = orderItem.quantity.toString()
            viewModel.updateOrderSummary("")
        }

        subtractButton.setOnClickListener {
            orderItem.quantity--
            if (orderItem.quantity < 0) {
                orderItem.quantity = 0
            }
            quantityText.text = orderItem.quantity.toString()
            viewModel.updateOrderSummary("")
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
            setupConfirmedItems()
        }
    }

    /**
     *
     */
    private fun setupConfirmedItems() {
        val actionsContainer = findViewById<ConstraintLayout>(R.id.actionsContainer)
        actionsContainer.visibility = View.GONE
        quantityText.setTextColor(context.getColor(R.color.black))
        statusBar.setBackgroundColor(context.getColor(R.color.success))
    }

    /**
     *
     */
    private fun initViewModel() {
        // Use the ViewModelProvider to get the ViewModel scoped to the parent (Activity/Fragment)
        val activity = context as? AppCompatActivity
        activity?.let {
            // ViewModelProvider will give you a ViewModel that is tied to the Activity's lifecycle
            viewModel = ViewModelProvider(it)[SummaryOrderViewModel::class.java]
        }
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
            viewModel.updateOrderSummary("")
        }

        // Show the popup
        customPopup.showPopup(editButton)
    }

}