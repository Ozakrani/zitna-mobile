package com.example.zitnamobile.repository

import com.example.zitnamobile.api.RetrofitClient
import com.example.zitnamobile.model.OrderRequest
import com.example.zitnamobile.model.OrderResponse

class OrderRepository {

    suspend fun createOrder(orderRequest: OrderRequest): String? {
        return try {
            val response = RetrofitClient.orderApi.createOrder(
                userId = "00000000-0000-0000-0000-000000000001",
                orderRequest = orderRequest
            )
            if (response.isSuccessful) {
                response.body() ?: "Commande créée !"
            } else {
                "Erreur ${response.code()}: ${response.errorBody()?.string()}"
            }
        } catch (e: Exception) {
            "Exception: ${e.message}"
        }
    }

    suspend fun getOrders(): List<OrderResponse> {
        return try {
            android.util.Log.d("ORDER_DEBUG", "=== Appel getOrders ===")
            val response = RetrofitClient.orderApi.getOrders(
                userId = "00000000-0000-0000-0000-000000000001"
            )
            android.util.Log.d("ORDER_DEBUG", "Code: ${response.code()}")
            android.util.Log.d("ORDER_DEBUG", "Body: ${response.body()}")
            android.util.Log.d("ORDER_DEBUG", "Error: ${response.errorBody()?.string()}")
            if (response.isSuccessful) response.body() ?: emptyList()
            else emptyList()
        } catch (e: Exception) {
            android.util.Log.e("ORDER_DEBUG", "EXCEPTION: ${e.message}")
            emptyList()
        }
    }

    suspend fun getOrder(orderId: String): OrderResponse? {
        return try {
            val response = RetrofitClient.orderApi.getOrder(orderId)
            if (response.isSuccessful) response.body()
            else null
        } catch (e: Exception) {
            null
        }
    }
}