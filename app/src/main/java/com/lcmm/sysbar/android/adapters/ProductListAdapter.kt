package com.lcmm.sysbar.android.adapters

import android.content.Context
import android.widget.RelativeLayout
import com.google.android.flexbox.FlexboxLayout
import com.lcmm.sysbar.android.components.ProductItemView
import com.lcmm.sysbar.android.models.Product

class ProductListAdapter (
    private val context: Context,
    private val flexboxLayout: FlexboxLayout,
    private val onItemClickListener: (Product) -> Unit // Callback to handle click
) {

    fun setItems(tables: List<Product>) {
        // Clear existing views
        flexboxLayout.removeAllViews()

        // Dynamically create views for each item and add to FlexboxLayout
        tables.forEach { product ->
            val productItem = ProductItemView(context).apply {
                setOnClickListener {
                    onItemClickListener(product)  // Trigger the click listener with the clicked item
                }
                val params = RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
                )
                val scale = resources.displayMetrics.density
                val marginSize = 5.0f
                val dpAsPixels = (marginSize * scale + 0.5f).toInt()
                params.setMargins(dpAsPixels, dpAsPixels, dpAsPixels, dpAsPixels)
                layoutParams = params
            }
            productItem.bindData(product)
            flexboxLayout.addView(productItem)
        }
    }
}