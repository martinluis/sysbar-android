package com.lcmm.sysbar.android.models

import com.lcmm.sysbar.android.enums.OrderType


data class PreparationQueueSummary(
     var orderId: Long,
     var destination: String?,
     var userName: String,
     var orderType: OrderType,
     val preparationQueueList: List<PreparationQueue>,
)
