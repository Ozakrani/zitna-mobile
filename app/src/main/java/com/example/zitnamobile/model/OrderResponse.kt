package com.example.zitnamobile.model

data class OrderResponse(
    val id: String,
    val userId: String,
    val status: String,
    val totalPrice: Double,
    val createdAt: String,
    val items: List<OrderItem>
)