package com.lcmm.sysbar.android.models

import com.lcmm.sysbar.android.enums.OrderType


data class PreparationQueue(
     var id: Long?,
     var orderId: Long?,
     var tableName: String,
     val userName: String?,
     val productName: String?,
     val quantity: Int?,
     val comment: String?,
     val orderType: OrderType?,
     val customer: Customer?
)
