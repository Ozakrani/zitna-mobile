package com.example.zitnamobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.zitnamobile.ui.OrderScreen
import com.example.zitnamobile.ui.theme.ZitnaMobileTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {

            ZitnaMobileTheme {

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    OrderScreen(
                        modifier = Modifier.padding(innerPadding)
                    )

                }

            }

        }

    }

}