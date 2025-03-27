package com.lcmm.sysbar.android.services

import com.lcmm.sysbar.android.models.PreparationQueue
import retrofit2.http.GET

interface PreparationQueueService {

    @GET("preparationQueue/findActive")
    suspend fun getActives(): List<PreparationQueue>

}