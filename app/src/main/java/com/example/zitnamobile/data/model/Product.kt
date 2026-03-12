package com.example.zitnamobile.data.model

data class Product(
    val id: String = "",        // UUID généré par Spring, vide à la création
    val name: String,
    val description: String,
    val type: String,
    val price: Double,          // BigDecimal Spring → Double Kotlin
    val stock: Int
)