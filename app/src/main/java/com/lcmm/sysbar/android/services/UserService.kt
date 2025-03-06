package com.lcmm.sysbar.android.services

import com.lcmm.sysbar.android.models.User
import retrofit2.http.GET
import retrofit2.http.Query

interface UserService {
    @GET("user/") // Adjust the endpoint as needed
    suspend fun getAll(): List<User>

    @GET("user/requestAccess") // Adjust the endpoint as needed
    suspend fun requestAccess(@Query("code") code: String): User
}