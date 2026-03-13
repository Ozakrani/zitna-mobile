package com.example.zitnamobile.api

import com.example.zitnamobile.model.ProductResponse
import retrofit2.Response
import retrofit2.http.GET

interface ProductApi {
    @GET("products")
    suspend fun getProducts(): Response<List<ProductResponse>>
}