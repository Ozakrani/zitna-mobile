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

    fun loadProducts() {

        viewModelScope.launch {

            try {

                val response = RetrofitInstance.api.getProducts()

                println("Produits reçus depuis API: $response")

                products = response
                filteredProducts = response

            } catch (e: Exception) {

                println("Erreur API: ${e.message}")
                e.printStackTrace()

            }
        }
    }
    fun filterProducts(keyword: String, type: String) {

        filteredProducts = products.filter {

            (keyword.isBlank() || it.name.contains(keyword, ignoreCase = true)) &&
                    (type.isBlank() || it.type.contains(type, ignoreCase = true))

        }
    }
}