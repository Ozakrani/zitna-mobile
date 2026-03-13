package com.example.zitnamobile.model

data class ProductResponse(
    val id: String,
    val name: String,
    val description: String,
    val price: Double,
    val stock: Int
)