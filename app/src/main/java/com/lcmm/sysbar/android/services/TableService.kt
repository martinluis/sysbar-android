package com.lcmm.sysbar.android.services

import com.lcmm.sysbar.android.models.Table
import retrofit2.http.GET
import retrofit2.http.Path

interface TableService {
    @GET("table")
    suspend fun getAll(): List<Table>

    @GET("table/{id}")
    suspend fun get(@Path("id") id:Long): Table
}