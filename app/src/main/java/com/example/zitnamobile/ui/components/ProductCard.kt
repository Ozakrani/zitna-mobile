package com.example.zitnamobile.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.zitnamobile.data.model.Product

@Composable
fun ProductCard(product: Product) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = product.name,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = product.description
            )

            Text(
                text = "Type : ${product.type}"
            )

            Text(
                text = "Prix : ${product.price} €"
            )

            Text(
                text = "Stock : ${product.stock}"
            )

        }
    }
}