package com.example.zitnamobile.data.repository

import com.example.zitnamobile.data.remote.ApiService
import com.example.zitnamobile.data.remote.dto.AuthResponse
import com.example.zitnamobile.data.remote.dto.LoginRequest
import com.example.zitnamobile.data.remote.dto.RegisterRequest

class AuthRepositoryImpl(private val apiService: ApiService) : AuthRepository {

    override suspend fun login(email: String, password: String): Result<AuthResponse> {
        return try {
            val response = apiService.login(LoginRequest(email, password))
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun register(...): Result<AuthResponse> {
        return try {
            val response = apiService.register(RegisterRequest(...))
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}