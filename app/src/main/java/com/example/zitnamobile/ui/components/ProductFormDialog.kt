package com.example.zitnamobile.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.zitnamobile.data.model.Product

@Composable
fun ProductFormDialog(
    product: Product? = null,
    onDismiss: () -> Unit,
    onConfirm: (Product) -> Unit
) {
    var name        by remember { mutableStateOf(product?.name        ?: "") }
    var description by remember { mutableStateOf(product?.description ?: "") }
    var type        by remember { mutableStateOf(product?.type        ?: "") }
    var price       by remember { mutableStateOf(product?.price?.toString() ?: "") }
    var stock       by remember { mutableStateOf(product?.stock?.toString() ?: "") }

    val isFormValid = name.isNotBlank()
            && price.toDoubleOrNull() != null
            && stock.toIntOrNull() != null

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (product == null) "Nouveau produit" else "Modifier le produit") },
        text = {
            Column(
                modifier = Modifier
                    .heightIn(max = 400.dp)   // ← hauteur max fixée
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nom *") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = name.isBlank(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = type,
                    onValueChange = { type = it },
                    label = { Text("Type") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = price,
                    onValueChange = { price = it },
                    label = { Text("Prix (€) *") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = price.toDoubleOrNull() == null,
                    singleLine = true
                )

                OutlinedTextField(
                    value = stock,
                    onValueChange = { stock = it },
                    label = { Text("Stock *") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = stock.toIntOrNull() == null,
                    singleLine = true
                )

                // ← espace en bas pour que Stock soit bien visible au scroll
                Spacer(modifier = Modifier.height(8.dp))
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm(
                        Product(
                            id          = product?.id ?: "",
                            name        = name.trim(),
                            description = description.trim(),
                            type        = type.trim(),
                            price       = price.toDoubleOrNull() ?: 0.0,
                            stock       = stock.toIntOrNull()    ?: 0
                        )
                    )
                },
                enabled = isFormValid
            ) {
                Text("Confirmer")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Annuler")
            }
        }
    )
}