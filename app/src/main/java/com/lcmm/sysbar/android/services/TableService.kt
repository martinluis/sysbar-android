package com.lcmm.sysbar.android.services

import com.lcmm.sysbar.android.models.Table
import retrofit2.http.GET
import retrofit2.http.Path

interface TableService {
    @GET("table/") // Adjust the endpoint as needed
    suspend fun getAll(): List<Table>

    @GET("table/{id}") // Adjust the endpoint as needed
    suspend fun get(@Path("id") id:Long): Table
}