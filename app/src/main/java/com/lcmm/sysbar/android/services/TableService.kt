package com.lcmm.sysbar.android.services

import com.lcmm.sysbar.android.models.Table
import retrofit2.http.GET

interface TableService {
    @GET("table/") // Adjust the endpoint as needed
    suspend fun getAll(): List<Table>
}