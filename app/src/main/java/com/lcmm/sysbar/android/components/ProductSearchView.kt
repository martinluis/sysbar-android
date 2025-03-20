package com.lcmm.sysbar.android.components

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.lcmm.sysbar.android.R
import com.lcmm.sysbar.android.models.Product
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
    private fun handleProductsResponse(products: List<Product>) {
        // Initialize the productItemAdapter and set it to the RecyclerView
        productItemAdapter = ProductItemAdapter(products) { product ->
            productViewModel.addProductToOrder(product)
        }
        recyclerView.adapter = productItemAdapter
    }

}


// ItemAdapter.kt
class ProductItemAdapter(private var items: List<Product>, private val onClickListener: (Product) -> Unit) : RecyclerView.Adapter<ProductItemAdapter.ProductItemViewHolder>() {

    private var filteredItems: List<Product> = items

    // ViewHolder that holds the reference to the custom view
    class ProductItemViewHolder(val productItemView: ProductItemView) : RecyclerView.ViewHolder(productItemView)

    /**
     *
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductItemViewHolder {
        // Inflate the custom view and return the ViewHolder
        val itemView = ProductItemView(parent.context)

        // Set FlexboxLayoutParams instead of the default LayoutParams
        val layoutParams = FlexboxLayoutManager.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        // Set margins
        val scale = 2
        val marginSize = 5.0f
        val dpAsPixels = (marginSize * scale + 0.5f).toInt()
        layoutParams.setMargins(dpAsPixels, dpAsPixels, dpAsPixels, dpAsPixels)

        itemView.layoutParams = layoutParams

        return ProductItemViewHolder(itemView)
    }

    /**
     *
     */
    override fun onBindViewHolder(holder: ProductItemViewHolder, position: Int) {
        val item = filteredItems[position]
        holder.productItemView.bindData(item)
        holder.productItemView.setClickListener {
            onClickListener(item)
        }
    }

    /**
     *
     */
    override fun getItemCount(): Int {
        return filteredItems.size
    }

    // Filter method to update the displayed items
    @SuppressLint("NotifyDataSetChanged")
    fun filter(query: String) {
        filteredItems = if (query.isEmpty()) {
            items
        } else {
            items.filter { it.name.contains(query, ignoreCase = true) }
        }
        notifyDataSetChanged()
    }
}