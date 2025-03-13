package com.lcmm.sysbar.android.components

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.text.TextPaint
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.google.android.flexbox.FlexboxLayout
import com.lcmm.sysbar.android.R
import com.lcmm.sysbar.android.adapters.ProductListAdapter
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

    private val productFlexbox: FlexboxLayout
    private lateinit var productViewModel: ProductViewModel

    init {
        // Inflate the custom layout
        LayoutInflater.from(context).inflate(R.layout.search_product_view, this, true)
        productFlexbox = findViewById(R.id.productFlexbox)

        initViewModels()
    }


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
        // Create an instance of TableListAdapter and define the click behavior
        val flexboxHelper = ProductListAdapter( context, productFlexbox) { item ->
            handleSelectProduct(item)
        }
        flexboxHelper.setItems( products )
    }

}