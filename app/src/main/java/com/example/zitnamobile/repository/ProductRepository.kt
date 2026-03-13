package com.example.zitnamobile.repository

import com.example.zitnamobile.api.RetrofitClient
import com.example.zitnamobile.model.ProductResponse

class ProductRepository {
    suspend fun getProducts(): List<ProductResponse> {
        return try {
            val response = RetrofitClient.productApi.getProducts()
            if (response.isSuccessful) response.body() ?: emptyList()
            else emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }
}