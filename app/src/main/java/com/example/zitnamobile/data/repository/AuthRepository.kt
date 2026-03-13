package com.example.zitnamobile.data.repository

import com.example.zitnamobile.data.remote.dto.AuthResponse

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<AuthResponse>
    suspend fun register(firstName: String, lastName: String, email: String,
                         password: String, phone: String): Result<AuthResponse>
}