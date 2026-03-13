package com.example.zitnamobile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.zitnamobile.data.remote.RetrofitInstance

class ProductViewModelFactory(private val token: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return ProductViewModel(RetrofitInstance.create(token)) as T
    }
}