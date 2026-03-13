package com.example.zitnamobile.model
import com.google.gson.annotations.SerializedName
data class OrderItem(
    @SerializedName("id") val id: String? = null,
    @SerializedName("productId") val productId: String? = null,
    @SerializedName("quantity") val quantity: Int? = null,
    @SerializedName("unitPrice") val unitPrice: Double? = null
)

data class OrderRequest(
    val items: List<OrderItem>
)