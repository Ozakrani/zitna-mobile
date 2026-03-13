package com.example.zitnamobile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zitnamobile.model.OrderItem
import com.example.zitnamobile.model.OrderRequest
import com.example.zitnamobile.model.OrderResponse
import com.example.zitnamobile.model.ProductResponse
import com.example.zitnamobile.repository.OrderRepository
import com.example.zitnamobile.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class OrderViewModel : ViewModel() {

    private val orderRepository = OrderRepository()
    private val productRepository = ProductRepository()

    private val _orderStatus = MutableStateFlow<String?>(null)
    val orderStatus: StateFlow<String?> = _orderStatus

    private val _orders = MutableStateFlow<List<OrderResponse>>(emptyList())
    val orders: StateFlow<List<OrderResponse>> = _orders

    private val _selectedOrder = MutableStateFlow<OrderResponse?>(null)
    val selectedOrder: StateFlow<OrderResponse?> = _selectedOrder

    private val _products = MutableStateFlow<List<ProductResponse>>(emptyList())
    val products: StateFlow<List<ProductResponse>> = _products

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun loadProducts() {
        viewModelScope.launch {
            _isLoading.value = true
            _products.value = productRepository.getProducts()
            _isLoading.value = false
        }
    }

    fun createOrder(productId: String, quantity: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            val orderRequest = OrderRequest(
                items = listOf(OrderItem(productId = productId, quantity = quantity))
            )
            _orderStatus.value = orderRepository.createOrder(orderRequest)
            _isLoading.value = false
        }
    }

    fun getOrders() {
        viewModelScope.launch {
            _isLoading.value = true
            _orders.value = orderRepository.getOrders()
            _isLoading.value = false
        }
    }

    fun getOrder(orderId: String) {
        viewModelScope.launch {
            _selectedOrder.value = orderRepository.getOrder(orderId)
        }
    }
}