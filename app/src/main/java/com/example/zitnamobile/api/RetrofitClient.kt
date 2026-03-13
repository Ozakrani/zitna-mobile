package com.example.zitnamobile.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
object RetrofitClient {

    private const val GATEWAY_URL = "http://10.0.2.2:8080/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(GATEWAY_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val orderApi: OrderApi by lazy {
        retrofit.create(OrderApi::class.java)
    }

    val productApi: ProductApi by lazy {
        retrofit.create(ProductApi::class.java)
    }
}