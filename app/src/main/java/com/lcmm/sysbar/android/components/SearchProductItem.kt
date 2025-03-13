package com.lcmm.sysbar.android.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.lcmm.sysbar.android.R
import com.lcmm.sysbar.android.models.Product
import com.lcmm.sysbar.android.utils.StringUtils

class SearchProductItem @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val productText: TextView
    private val priceText: TextView


    init {
        // Inflate the custom layout
        LayoutInflater.from(context).inflate(R.layout.product_item, this, true)
        productText  = findViewById(R.id.productText)
        priceText  = findViewById(R.id.priceText)
    }



    /**
     *
     */
    fun setProduct(product: Product) {
        productText.text = product.name
        priceText.text = StringUtils.decimalToCurrencyFormat(product.price)
    }

}