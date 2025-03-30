package com.lcmm.sysbar.android.models

import com.lcmm.sysbar.android.enums.OrderType
import java.util.Date


data class PreparationQueueSummary(
     var orderId: Long,
     var destination: String?,
     var userName: String,
     var orderType: OrderType,
     var createdAt: Date,
     val preparationQueueList: List<PreparationQueue>,
)
