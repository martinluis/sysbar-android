package com.lcmm.sysbar.android.services

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL = "http://192.168.1.68:8080/sysbar/api/"
    //private const val BASE_URL = "http://10.0.2.2:8080/sysbar/api/"
    private const val API_KEY = "Us73mPtWRFhtcqm57Jf7VI5pmsT7KzN5kM5mYJDZn2sdp0zerPy3Zu06z36KelX1"

    // Create an Interceptor to add the API key to the header
    private val apiKeyInterceptor = Interceptor { chain ->
        val newRequest = chain.request().newBuilder()
            .addHeader("API-Key", API_KEY) // Add API key in Authorization header
            .build()
        chain.proceed(newRequest)
    }

    // Use the Interceptor in OkHttpClient
    private val client = OkHttpClient.Builder()
        .addInterceptor(apiKeyInterceptor)
        .build()

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)  // Attach the client with interceptor and ApiKey
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // --- List of Services --
    val userService: UserService by lazy {
        retrofit.create(UserService::class.java)
    }

    val tableService: TableService by lazy {
        retrofit.create(TableService::class.java)
    }

    val orderService: OrderService by lazy {
        retrofit.create(OrderService::class.java)
    }
}