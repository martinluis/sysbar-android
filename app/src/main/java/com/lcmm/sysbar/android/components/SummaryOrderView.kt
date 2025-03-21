package com.lcmm.sysbar.android.components

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.lcmm.sysbar.android.R
import com.lcmm.sysbar.android.models.Order
import com.lcmm.sysbar.android.models.OrderItem
import com.lcmm.sysbar.android.utils.StringUtils
import com.lcmm.sysbar.android.utils.ViewUtils
import com.lcmm.sysbar.android.viewModel.SummaryOrderViewModel
import java.math.BigDecimal


class SummaryOrderView@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val confirmButton: MaterialButton
    private val cancelButton: MaterialButton
    private val titleText: TextView
    private val totalText: TextView
    private val recyclerView: RecyclerView

    private var summaryActionsListener: OrderSummaryActionsListener? = null
    private var orderItemAdapter: OrderItemAdapter? = null

    private lateinit var viewModel: SummaryOrderViewModel

    private var order: Order? = null

    /**
     *
     */
    init {
        LayoutInflater.from(context).inflate(R.layout.summary_order_view, this, true)

        confirmButton = findViewById(R.id.confirmButton)
        cancelButton = findViewById(R.id.cancelButton)
        titleText = findViewById(R.id.titleText)
        totalText = findViewById(R.id.summaryTotalText)
        recyclerView =  findViewById(R.id.orderItemsList)

        val args = context.obtainStyledAttributes(attrs, R.styleable.SummaryOrderView, defStyleAttr, 0)
        args.recycle()
        initListeners()
        initViewModel()
    }

    /**
     *
     */
    @SuppressLint("NotifyDataSetChanged")
    private fun initView(order: Order){

        titleText.text = order.table.name

        val items = order.items?.toMutableList() ?: mutableListOf()

        // Set up the OrderItem list with Adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
        orderItemAdapter = OrderItemAdapter(items) { position ->
            items.removeAt(position)
            updateView()
        }
        recyclerView.adapter = orderItemAdapter
        val dividerItemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        recyclerView.addItemDecoration(dividerItemDecoration)

        updateView()
    }

    private fun initViewModel() {
        // Use the ViewModelProvider to get the ViewModel scoped to the parent (Activity/Fragment)
        val activity = context as? AppCompatActivity
        activity?.let {
            // ViewModelProvider will give you a ViewModel that is tied to the Activity's lifecycle
            viewModel = ViewModelProvider(it)[SummaryOrderViewModel::class.java]
            viewModel.orderSummary.observe(it, Observer {
                updateView()
            })
        }
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

    /**
     *
     */
    fun addItem(item: OrderItem){
        val itemsList = getNewItems().toMutableList()
        val foundItem = itemsList.find { it.productId == item.productId && it.comment.isEmpty() }
        if (foundItem!=null) {
            foundItem.quantity += item.quantity
            val index = itemsList.indexOfFirst { it == foundItem}
            itemsList[index] = foundItem
        }
        else{
            orderItemAdapter?.addItem(item)
        }
        updateView()

        // Move to index location of the item
        getNewItems().forEachIndexed { index, it ->
           if (it.productId == item.productId) {
               recyclerView.post {
                   recyclerView.scrollToPosition(index)
               }
               return
           }
        }

    }

    /**
     *
     */
    @SuppressLint("NotifyDataSetChanged")
    fun removeNewItems(){
        val newItems = getNewItems() as MutableList
        orderItemAdapter?.getItems()?.removeAll(newItems)
        updateView()
    }

    /**
     *
     */
    fun getNewItems(): List<OrderItem>{
        val items = orderItemAdapter?.getItems()
        items?.let {
            return items.filter { it.itemId == null }
        }
        return listOf()
    }

    /**
     *
     */
    private fun getAllItems(): List<OrderItem>{
        val items = orderItemAdapter?.getItems()
        items?.let {
            return items
        }
        return listOf()
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
     *
     */
    @SuppressLint("NotifyDataSetChanged")
    private fun updateView() {
        orderItemAdapter?.notifyDataSetChanged()
        totalText.text = getTotalFromItems(getAllItems())
        val newItemsFiltered = getNewItems().filter { it.quantity > 0 }
        ViewUtils.toggleButton(context, confirmButton, newItemsFiltered.isNotEmpty(), R.color.success)
        ViewUtils.toggleButton(context, cancelButton, newItemsFiltered.isNotEmpty(), R.color.warning)
    }

}

/**
 * Adapter for list of OrderItems in Summary
 */
class OrderItemAdapter(private var items: MutableList<OrderItem>, private val onItemDelete: (Int) -> Unit) : RecyclerView.Adapter<OrderItemAdapter.OrderItemViewHolder>() {

    // ViewHolder that holds the reference to the custom view
    class OrderItemViewHolder(val orderItemView: OrderItemView) : RecyclerView.ViewHolder(orderItemView)

    /**
     *
     */
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

    /**
     *
     */
    override fun onBindViewHolder(holder: OrderItemViewHolder, position: Int) {
        val item = items[position]
        holder.orderItemView.bindData(item)
        // Set the delete button listener
        holder.orderItemView.setDeleteClickListener {
            onItemDelete(position)
        }
    }

    /**
     *
     */
    override fun getItemCount(): Int {
        return items.size
    }

    /*8

     */
    fun addItem(item: OrderItem) {
        items.add(item)
        notifyItemInserted(items.size + 1)  // New item added at the end of newItemList
    }

    /**
     *
     */
    fun getItems(): MutableList<OrderItem>{
        return items
    }
}


/**
 *
 */
interface OrderSummaryActionsListener {
    fun onConfirmButtonClick()
    fun onCancelButtonClick()
}
