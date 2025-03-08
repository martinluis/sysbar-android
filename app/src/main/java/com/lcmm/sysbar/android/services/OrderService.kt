package com.lcmm.sysbar.android.services

import com.lcmm.sysbar.android.models.Order
import retrofit2.http.GET
import retrofit2.http.Query

interface OrderService {

    @GET("order/findByTable")
    suspend fun findByTable(@Query("tableId") tableId: Long): Order
}