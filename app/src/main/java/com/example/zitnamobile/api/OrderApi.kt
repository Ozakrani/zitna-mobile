package com.example.zitnamobile.api

import com.example.zitnamobile.model.OrderRequest
import com.example.zitnamobile.model.OrderResponse
import retrofit2.Response
import retrofit2.http.*

interface OrderApi {

    @POST("api/v1/orders")
    suspend fun createOrder(
        @Header("X-USER-ID") userId: String,
        @Body orderRequest: OrderRequest
    ): Response<String>

    @GET("api/v1/orders")
    suspend fun getOrders(
        @Header("X-USER-ID") userId: String
    ): Response<List<OrderResponse>>

    @GET("api/v1/orders/{id}")
    suspend fun getOrder(
        @Path("id") orderId: String
    ): Response<OrderResponse>
}