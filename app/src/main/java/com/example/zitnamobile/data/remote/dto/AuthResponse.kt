package com.example.zitnamobile.data.remote.dto

data class AuthResponse(
    val accessToken: String,
    val tokenType: String,
    val expiresIn: Int,
    val user: UserInfo
)