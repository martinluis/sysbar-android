package com.lcmm.sysbar.android.components

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.text.TextPaint
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayout
import com.google.android.flexbox.FlexboxLayoutManager
import com.lcmm.sysbar.android.R
import com.lcmm.sysbar.android.adapters.ProductListAdapter
import com.lcmm.sysbar.android.models.Product
import com.lcmm.sysbar.android.utils.StringUtils
import com.lcmm.sysbar.android.viewModel.ProductViewModel

/**
 * TODO: document your custom view class.
 */
class SearchProductView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayoutCompat(context, attrs, defStyleAttr) {

    private lateinit var recyclerView: RecyclerView
    private lateinit var filterEditText: EditText
    private lateinit var productItemAdapter: ProductItemAdapter
   


    private lateinit var productViewModel: ProductViewModel

    init {
        // Inflate the custom layout
        LayoutInflater.from(context).inflate(R.layout.product_search_view, this, true)

        initView()
        initListeners()
        initViewModels()
    }

    /**
     *
     */
    private fun initView() {
        recyclerView = findViewById(R.id.recyclerView)
        filterEditText = findViewById(R.id.filterEditText)

        val flexboxLayoutManager = FlexboxLayoutManager(context)
        flexboxLayoutManager.flexWrap = FlexWrap.WRAP  // Allow wrapping
        recyclerView.layoutManager = flexboxLayoutManager
    }

    /**
     *
     */
    private fun initListeners() {
        // Set up text change listener for filtering items
        filterEditText.addTextChangedListener { editable ->
            val query = editable.toString()
            productItemAdapter.filter(query)
        }

        // Optionally, you can filter only when the "Enter" key is pressed
        filterEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val query = filterEditText.text.toString()
                productItemAdapter.filter(query)
                true
            } else {
                false
            }
        }
    }

    /**
     *
     */
    private fun initViewModels() {
        // Use the ViewModelProvider to get the ViewModel scoped to the parent (Activity/Fragment)
        val activity = context as? AppCompatActivity
        activity?.let {
            // ViewModel for Product
            productViewModel = ViewModelProvider(it)[ProductViewModel::class.java]
            productViewModel.productsLiveData.observe(it) { products ->
                handleProductsResponse(products)
            }
            productViewModel.fetchProducts()

        }
    }


    /**
     *
     */
    private fun handleSelectProduct(product: Product){
        productViewModel.addProductToOrder(product)
    }

    /**
     *
     */
    private fun handleProductsResponse(products: List<Product>) {
        // Initialize the productItemAdapter and set it to the RecyclerView
        productItemAdapter = ProductItemAdapter(products)
        recyclerView.adapter = productItemAdapter
    }

}


// ItemAdapter.kt
class ProductItemAdapter(private val originalItems: List<Product>) : RecyclerView.Adapter<ProductItemAdapter.ViewHolder>() {

    private var filteredItems: List<Product> = originalItems

    // ViewHolder to hold each item
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productText: TextView = itemView.findViewById(R.id.productText)
        val priceText: TextView = itemView.findViewById(R.id.priceText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_item_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.productText.text = filteredItems[position].name
        holder.priceText.text = StringUtils.decimalToCurrencyFormat( filteredItems[position].price )
    }

    override fun getItemCount(): Int = filteredItems.size

    // Filter method to update the displayed items
    @SuppressLint("NotifyDataSetChanged")
    fun filter(query: String) {
        filteredItems = if (query.isEmpty()) {
            originalItems
        } else {
            originalItems.filter { it.name.contains(query, ignoreCase = true) }
        }
        notifyDataSetChanged()
    }
}