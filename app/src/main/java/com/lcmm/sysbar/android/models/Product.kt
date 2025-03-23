package com.lcmm.sysbar.android.models

import com.lcmm.sysbar.android.enums.ProductType
import java.math.BigDecimal

data class Product(
    var id: Long? = null,
    var name: String,
    var price: BigDecimal,
    var stock: Int,
    var type: ProductType,
    var active: Boolean
)
