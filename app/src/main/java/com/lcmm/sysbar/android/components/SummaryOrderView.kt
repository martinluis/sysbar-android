package com.lcmm.sysbar.android.components

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.lcmm.sysbar.android.R
import com.lcmm.sysbar.android.models.Order
import com.lcmm.sysbar.android.models.OrderItem
import com.lcmm.sysbar.android.utils.StringUtils
import java.math.BigDecimal


class SummaryOrderView@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var confirmButton: MaterialButton
    private var cancelButton: MaterialButton
    private var titleText: TextView

    private var summaryActionsListener: OrderSummaryActionsListener? = null
    private var orderItemAdapter: OrderItemAdapter? = null

    private var order: Order? = null
    private var orderItemsAdded: List<OrderItem> = mutableListOf()

    /**
     *
     */
    init {
        LayoutInflater.from(context).inflate(R.layout.summary_order_view, this, true)

        confirmButton = findViewById(R.id.confirmButton)
        cancelButton = findViewById(R.id.cancelButton)
        titleText = findViewById(R.id.titleText)

        val args = context.obtainStyledAttributes(attrs, R.styleable.SummaryOrderView, defStyleAttr, 0)
        args.recycle()
        initListeners()
    }

    /**
     *
     */
    @SuppressLint("NotifyDataSetChanged")
    private fun initView(order: Order){
        if (order.items == null) {
            return
        }

        titleText.text = "Mesa 1"

        val items = order.items as MutableList<OrderItem>

        // Set up the OrderItem list
        val recyclerView = findViewById<RecyclerView>(R.id.orderItemsList)
        recyclerView.layoutManager = LinearLayoutManager(context)
        orderItemAdapter = OrderItemAdapter(items) { position ->
            items.removeAt(position)
            orderItemAdapter?.notifyItemRemoved(position)
        }
        recyclerView.adapter = orderItemAdapter
        val dividerItemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        recyclerView.addItemDecoration(dividerItemDecoration)
        val totalText = findViewById<TextView>(R.id.summaryTotalText)
        totalText.text = getTotalFromItems(items)
    }

    /**
     *
     */
    private fun initListeners(){
        confirmButton.setOnClickListener {
            // Notify the parent when the button is clicked
            summaryActionsListener?.onConfirmButtonClick()
        }

        cancelButton.setOnClickListener {
            // Notify the parent when the button is clicked
            summaryActionsListener?.onCancelButtonClick()
        }
    }

    /**
     *
     */
    private fun getTotalFromItems(items: List<OrderItem>): String{
        var total = BigDecimal.ZERO
        items.forEach {
            total = total.add(it.getTotal())
        }
        return StringUtils.decimalToCurrencyFormat(total)
    }

    /**
     * Method to set the callback listener
     */
    fun setOrderSummaryActionsListener(listener: OrderSummaryActionsListener) {
        this.summaryActionsListener = listener
    }

    /**
     * Setter method to pass the object after the view is initialized
     */
    fun setOrder(order: Order) {
        this.order = order
        initView(order)
    }

}

/**
 * Adapter for list of OrderItems in Summary
 */
class OrderItemAdapter(private var items: List<OrderItem>, private val onItemDelete: (Int) -> Unit) : RecyclerView.Adapter<OrderItemAdapter.OrderItemViewHolder>() {

    // ViewHolder that holds the reference to the custom view
    class OrderItemViewHolder(val orderItemView: OrderItemView) : RecyclerView.ViewHolder(orderItemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderItemViewHolder {
        // Inflate the custom view and return the ViewHolder
        val itemView = OrderItemView(parent.context)

        // manually set the CustomView's size - this is what I was missing
        itemView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        return OrderItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: OrderItemViewHolder, position: Int) {
        val item = items[position]
        holder.orderItemView.bindData(item)
        // Set the delete button listener
        holder.orderItemView.setDeleteClickListener {
            onItemDelete(position)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}



/**
 *
 */
interface OrderSummaryActionsListener {
    fun onConfirmButtonClick()
    fun onCancelButtonClick()
}
