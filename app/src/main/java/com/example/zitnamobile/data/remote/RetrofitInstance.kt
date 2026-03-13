package com.example.zitnamobile.data.remote

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitInstance {
    private const val BASE_URL = "http://172.20.10.4:8090/"

    // Utilisé par le ProductViewModel par défaut (sans token)
    val api: ProductApiService by lazy { create("") }

    fun create(token: String): ProductApiService {
        val client = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .apply {
                if (token.isNotBlank()) {
                    addInterceptor { chain ->
                        val request = chain.request().newBuilder()
                            .addHeader("Authorization", "Bearer $token")
                            .build()
                        chain.proceed(request)
                    }
                }
            }
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ProductApiService::class.java)
    }
}