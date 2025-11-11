package com.lcmm.sysbar.android.models

import com.lcmm.sysbar.android.enums.OrderType
import java.time.LocalDateTime


data class PreparationQueueSummary(
     var orderId: Long,
     var destination: String?,
     var userName: String,
     var orderType: OrderType,
     var createdAt: LocalDateTime,
     val preparationQueueList: List<PreparationQueue>,
)
