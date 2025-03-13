package com.lcmm.sysbar.android.services

import com.lcmm.sysbar.android.models.Product
import retrofit2.http.GET

interface ProductService {
    @GET("product/")
    suspend fun getAll(): List<Product>
}