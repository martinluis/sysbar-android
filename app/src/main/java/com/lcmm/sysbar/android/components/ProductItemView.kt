package com.lcmm.sysbar.android.components

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.lcmm.sysbar.android.R
import com.lcmm.sysbar.android.models.Product
import com.lcmm.sysbar.android.utils.StringUtils


class ProductItemView@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val productText: TextView
    private val priceText: TextView


    /**
     *
     */
    init {
        LayoutInflater.from(context).inflate(R.layout.product_item_view, this, true)

        productText = findViewById(R.id.productText)
        priceText = findViewById(R.id.priceText)
    }


    /**
     *
     */
    @SuppressLint("SetTextI18n")
    fun bindData(product: Product){
        productText.text = product.name
        priceText.text = StringUtils.decimalToCurrencyFormat( product.price )
    }


    /**
     *
     */
    fun setClickListener(listener: OnClickListener) {
        this.setOnClickListener(listener)
    }

}