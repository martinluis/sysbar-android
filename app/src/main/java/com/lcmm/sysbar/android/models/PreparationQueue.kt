package com.lcmm.sysbar.android.models

import com.lcmm.sysbar.android.enums.OrderType
import java.util.Date


data class PreparationQueue(
     var id: Long?,
     var orderId: Long?,
     var tableName: String,
     val userName: String?,
     val productName: String?,
     val quantity: Int?,
     val comment: String?,
     val orderType: OrderType?,
     val createdAt: Date,
     val customer: Customer?
)
