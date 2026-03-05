package com.example.zitnamobile.ui

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun OrderScreen(modifier: Modifier = Modifier) {

    Button(
        onClick = {
            println("Create Order clicked")
        },
        modifier = modifier
    ) {
        Text("Create Order")
    }
}