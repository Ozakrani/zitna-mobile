package com.example.zitnamobile.data.remote

import com.example.zitnamobile.data.remote.dto.AuthResponse
import com.example.zitnamobile.data.remote.dto.LoginRequest
import com.example.zitnamobile.data.remote.dto.RegisterRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): AuthResponse

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): AuthResponse
}