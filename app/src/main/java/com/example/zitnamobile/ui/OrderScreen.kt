package com.example.zitnamobile.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.zitnamobile.model.OrderResponse
import com.example.zitnamobile.model.ProductResponse
import com.example.zitnamobile.viewmodel.OrderViewModel

@Composable
fun OrderScreen(modifier: Modifier = Modifier) {

    val viewModel: OrderViewModel = viewModel()
    val orderStatus by viewModel.orderStatus.collectAsState()
    val orders by viewModel.orders.collectAsState()
    val selectedOrder by viewModel.selectedOrder.collectAsState()
    val products by viewModel.products.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    var quantity by remember { mutableStateOf("1") }
    var searchOrderId by remember { mutableStateOf("") }
    var selectedTab by remember { mutableIntStateOf(0) }
    var selectedProduct by remember { mutableStateOf<ProductResponse?>(null) }

    // Charger les produits au démarrage
    LaunchedEffect(Unit) {
        viewModel.loadProducts()
    }

    Column(modifier = modifier.fillMaxSize()) {

        TabRow(selectedTabIndex = selectedTab) {
            Tab(selected = selectedTab == 0, onClick = { selectedTab = 0 }) {
                Text("Créer", modifier = Modifier.padding(12.dp))
            }
            Tab(selected = selectedTab == 1, onClick = {
                selectedTab = 1
                viewModel.getOrders()
            }) {
                Text("Mes commandes", modifier = Modifier.padding(12.dp))
            }
            Tab(selected = selectedTab == 2, onClick = { selectedTab = 2 }) {
                Text("Chercher", modifier = Modifier.padding(12.dp))
            }
        }

        when (selectedTab) {

            // ── Onglet Créer ──
            0 -> Column(
                modifier = Modifier.fillMaxSize().padding(16.dp)
            ) {
                Text("Choisir un produit", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))

                if (isLoading && products.isEmpty()) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                } else if (products.isEmpty()) {
                    Text("Aucun produit disponible.", color = MaterialTheme.colorScheme.error)
                } else {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(products) { product ->
                            ProductCard(
                                product = product,
                                isSelected = selectedProduct?.id == product.id,
                                onClick = { selectedProduct = product }
                            )
                        }
                    }
                }

                Spacer(Modifier.height(12.dp))

                selectedProduct?.let { product ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("Produit sélectionné : ${product.name}", style = MaterialTheme.typography.bodyMedium)
                            Text("Prix : ${product.price} €", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                }

                OutlinedTextField(
                    value = quantity,
                    onValueChange = { quantity = it },
                    label = { Text("Quantité") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(Modifier.height(12.dp))

                Button(
                    onClick = {
                        selectedProduct?.let { product ->
                            val qty = quantity.toIntOrNull() ?: 1
                            viewModel.createOrder(product.id, qty)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = selectedProduct != null && quantity.isNotBlank() && !isLoading
                ) {
                    Text("Créer la commande")
                }

                orderStatus?.let {
                    Spacer(Modifier.height(12.dp))
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Text(text = it, modifier = Modifier.padding(16.dp))
                    }
                }
            }

            // ── Onglet Mes commandes ──
            1 -> Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                Text("Mes commandes", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(16.dp))

                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                } else if (orders.isEmpty()) {
                    Text("Aucune commande trouvée.")
                } else {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(orders) { order -> OrderCard(order) }
                    }
                }
            }

            // ── Onglet Chercher ──
            2 -> Column(
                modifier = Modifier.fillMaxSize().padding(24.dp)
            ) {
                Text("Chercher une commande", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(16.dp))

                OutlinedTextField(
                    value = searchOrderId,
                    onValueChange = { searchOrderId = it },
                    label = { Text("Order ID") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(Modifier.height(12.dp))

                Button(
                    onClick = { viewModel.getOrder(searchOrderId) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = searchOrderId.isNotBlank()
                ) {
                    Text("Rechercher")
                }

                Spacer(Modifier.height(16.dp))
                selectedOrder?.let { OrderCard(it) }
            }
        }
    }
}

@Composable
fun ProductCard(
    product: ProductResponse,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected)
                MaterialTheme.colorScheme.primaryContainer
            else
                MaterialTheme.colorScheme.surface
        ),
        border = if (isSelected) CardDefaults.outlinedCardBorder() else null
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(product.name, style = MaterialTheme.typography.bodyLarge)
            Text(product.description, style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text("${product.price} € — Stock : ${product.stock}",
                style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
fun OrderCard(order: OrderResponse) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("ID : ${order.id}", style = MaterialTheme.typography.bodySmall)
            Text("Statut : ${order.status}")
            Text("Total : ${order.totalPrice} €")
            Text("Date : ${order.createdAt}")
        }
    }
}