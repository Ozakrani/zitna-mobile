package com.example.zitnamobile.data.remote.dto

data class UserInfo(
    val id: Long,
    val email: String,
    val firstName: String,
    val lastName: String,
    val role: String
)