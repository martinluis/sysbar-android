package com.lcmm.sysbar.android.services

import com.lcmm.sysbar.android.models.Order
import com.lcmm.sysbar.android.models.OrderItem
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface OrderService {

    @GET("order/findByTable")
    suspend fun findByTable(@Query("tableId") tableId: Long): Order


    @POST("order/{id}/addItems")
    suspend fun addItems(@Path("id") orderId: Long, @Body orderItems: List<OrderItem>): Order
}