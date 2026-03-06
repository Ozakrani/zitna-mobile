package com.example.zitnamobile.data.remote

import com.example.zitnamobile.data.model.Product
import retrofit2.http.GET

interface ProductApiService {

    @GET("products")
    suspend fun getProducts(): List<Product>

}