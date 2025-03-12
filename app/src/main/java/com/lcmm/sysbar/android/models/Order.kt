package com.lcmm.sysbar.android.models

import com.lcmm.sysbar.android.enums.OrderStatus
import com.lcmm.sysbar.android.enums.OrderType
import java.math.BigDecimal


data class Order (
    var id: Long? = null,
    var user: User,
    var table: Table,
    //var customer: Customer,
    var items: MutableList<OrderItem>?,
    var orderType: OrderType,
    var status: OrderStatus,
    var subtotal: BigDecimal,
    var total: BigDecimal,
    var discount: BigDecimal,

)