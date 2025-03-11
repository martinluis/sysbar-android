package com.lcmm.sysbar.android.models

import java.math.BigDecimal

data class OrderItem(
     var itemId: Long?,
     var productId: Long?,
     var productName: String,
     var productPrice: BigDecimal,
     var quantity: Int,
     var comment: String = ""
){
     fun getTotal(): BigDecimal {
          return productPrice.multiply(quantity.toBigDecimal())
     }
}
