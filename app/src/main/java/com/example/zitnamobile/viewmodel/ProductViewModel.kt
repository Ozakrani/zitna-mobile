package com.example.zitnamobile.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zitnamobile.data.model.Product
import com.example.zitnamobile.data.remote.RetrofitInstance
import kotlinx.coroutines.launch

class ProductViewModel : ViewModel() {

    var products by mutableStateOf<List<Product>>(emptyList())
        private set

    var filteredProducts by mutableStateOf<List<Product>>(emptyList())
        private set

    var searchKeyword by mutableStateOf("")
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun loadProducts() {
        viewModelScope.launch {
            isLoading = true
            try {
                val response = RetrofitInstance.api.getProducts()
                products = response
                applyFilter()
            } catch (e: Exception) {
                errorMessage = "Erreur chargement : ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun onSearchChanged(keyword: String) {
        searchKeyword = keyword
        applyFilter()
    }

    private fun applyFilter() {
        filteredProducts = if (searchKeyword.isBlank()) {
            products
        } else {
            products.filter {
                it.name.contains(searchKeyword, ignoreCase = true)
            }
        }
    }

    fun createProduct(product: Product, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                RetrofitInstance.api.createProduct(product)
                loadProducts()
                onSuccess()
            } catch (e: Exception) {
                errorMessage = "Erreur création : ${e.message}"
            }
        }
    }

    fun updateProduct(product: Product, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                RetrofitInstance.api.updateProduct(product.id, product)
                loadProducts()
                onSuccess()
            } catch (e: Exception) {
                errorMessage = "Erreur modification : ${e.message}"
            }
        }
    }

    fun deleteProduct(productId: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.deleteProduct(productId)
                if (response.isSuccessful || response.code() == 204) {
                    loadProducts()  // recharge la liste après suppression
                } else {
                    errorMessage = "Erreur suppression : code ${response.code()}"
                }
            } catch (e: Exception) {
                errorMessage = "Erreur suppression : ${e.message}"
            }
        }
    }
    fun clearError() {
        errorMessage = null
    }
}