package com.lcmm.sysbar.android.services

import com.lcmm.sysbar.android.models.PreparationQueue
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface PreparationQueueService {

    @GET("preparationQueue/findActive")
    suspend fun getActives(): List<PreparationQueue>

    @PUT("preparationQueue/{id}/finish")
    suspend fun finish(@Path("id") id: Long): Response<Unit>

}