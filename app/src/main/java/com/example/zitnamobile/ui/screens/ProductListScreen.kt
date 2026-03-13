package com.example.zitnamobile.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.zitnamobile.data.model.Product
import com.example.zitnamobile.ui.components.ProductCard
import com.example.zitnamobile.ui.components.ProductFormDialog
import com.example.zitnamobile.viewmodel.ProductViewModel

@Composable
fun ProductListScreen(
    modifier: Modifier = Modifier,
    viewModel: ProductViewModel = viewModel()
) {
    val products     = viewModel.filteredProducts
    val searchText   = viewModel.searchKeyword
    val isLoading    = viewModel.isLoading
    val errorMessage = viewModel.errorMessage

    var showCreateDialog by remember { mutableStateOf(false) }
    var productToEdit    by remember { mutableStateOf<Product?>(null) }

    LaunchedEffect(Unit) { viewModel.loadProducts() }

    if (showCreateDialog) {
        ProductFormDialog(
            onDismiss = { showCreateDialog = false },
            onConfirm = { newProduct ->
                viewModel.createProduct(newProduct) { showCreateDialog = false }
            }
        )
    }

    productToEdit?.let { product ->
        ProductFormDialog(
            product   = product,
            onDismiss = { productToEdit = null },
            onConfirm = { updated ->
                viewModel.updateProduct(updated) { productToEdit = null }
            }
        )
    }

    errorMessage?.let { msg ->
        AlertDialog(
            onDismissRequest = { viewModel.clearError() },
            title = { Text("Erreur") },
            text  = { Text(msg) },
            confirmButton = {
                Button(onClick = { viewModel.clearError() }) { Text("OK") }
            }
        )
    }

    Box(modifier = modifier.fillMaxSize()) {

        // ← LazyColumn gère TOUT le scroll, y compris le header
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = 8.dp,
                bottom = 80.dp   // espace pour le FAB
            ),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // Header titre + search comme items fixes
            item {
                Text(text = "Liste des produits", fontSize = 22.sp)
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = searchText,
                    onValueChange = { viewModel.onSearchChanged(it) },
                    label = { Text("Recherche par nom") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                if (isLoading) {
                    CircularProgressIndicator()
                } else {
                    Text(
                        text = "${products.size} produit(s) trouvé(s)",
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
            }

            // Liste des produits
            items(products, key = { it.id }) { product ->
                ProductCard(
                    product  = product,
                    onEdit   = { productToEdit = it },
                    onDelete = { viewModel.deleteProduct(it) }
                )
            }
        }

        // FAB en bas à droite
        FloatingActionButton(
            onClick = { showCreateDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Ajouter un produit")
        }
    }
}