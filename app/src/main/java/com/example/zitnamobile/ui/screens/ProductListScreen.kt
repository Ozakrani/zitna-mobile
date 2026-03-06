package com.example.zitnamobile.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.zitnamobile.viewmodel.ProductViewModel
import com.example.zitnamobile.ui.components.ProductCard

@Composable
fun ProductListScreen(
    modifier: Modifier = Modifier,
    viewModel: ProductViewModel = viewModel()
) {

    var searchText by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("") }

    val products = viewModel.filteredProducts

    LaunchedEffect(Unit) {
        viewModel.loadProducts()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Liste des produits",
            fontSize = 22.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            label = { Text("Recherche produit") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = type,
            onValueChange = { type = it },
            label = { Text("Type") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                viewModel.filterProducts(searchText, type)
            }
        ) {
            Text("Filtrer")
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {

            items(products) { product ->

                ProductCard(product)

            }

        }
    }
}