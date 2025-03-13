package com.lcmm.sysbar.android.models

import java.math.BigDecimal

data class Product(
    var id: Long? = null,
    var name: String,
    var price: BigDecimal,
    var stock: Int,
    var active: Boolean
)
