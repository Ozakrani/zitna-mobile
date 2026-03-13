package com.example.zitnamobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.zitnamobile.data.remote.RetrofitClient
import com.example.zitnamobile.data.repository.AuthRepositoryImpl
import com.example.zitnamobile.ui.OrderScreen
import com.example.zitnamobile.ui.auth.AuthUiState
import com.example.zitnamobile.ui.auth.AuthViewModel
import com.example.zitnamobile.ui.auth.AuthViewModelFactory
import com.example.zitnamobile.ui.auth.LoginScreen
import com.example.zitnamobile.ui.auth.RegisterScreen
import com.example.zitnamobile.ui.screens.ProductListScreen
import com.example.zitnamobile.ui.theme.ZitnaMobileTheme
import com.example.zitnamobile.viewmodel.ProductViewModel
import com.example.zitnamobile.viewmodel.ProductViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val repository = AuthRepositoryImpl(RetrofitClient.apiService)
        val factory = AuthViewModelFactory(repository)

        setContent {
            ZitnaMobileTheme {
                val navController = rememberNavController()
                val authViewModel: AuthViewModel = viewModel(factory = factory)
                var authToken by remember { mutableStateOf("") }
                var userRole by remember { mutableStateOf("") }

                NavHost(navController = navController, startDestination = "login") {

                    composable("login") {
                        LoginScreen(
                            viewModel = authViewModel,
                            onLoginSuccess = {
                                val state = authViewModel.uiState.value
                                if (state is AuthUiState.Success) {
                                    authToken = state.response.accessToken
                                    userRole = state.response.user.role

                                    // ← Injecter le token dans le RetrofitClient des orders
                                    com.example.zitnamobile.api.RetrofitClient.setToken(authToken)

                                    navController.navigate("home") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                }
                            },
                            onNavigateToRegister = { navController.navigate("register") }
                        )
                    }

                    composable("register") {
                        RegisterScreen(
                            viewModel = authViewModel,
                            onRegisterSuccess = {
                                navController.navigate("home") {
                                    popUpTo("register") { inclusive = true }
                                }
                            },
                            onNavigateToLogin = { navController.popBackStack() }
                        )
                    }

                    composable("home") {
                        val homeNavController = rememberNavController()
                        val navBackStackEntry by homeNavController.currentBackStackEntryAsState()
                        val currentRoute = navBackStackEntry?.destination?.route

                        Scaffold(
                            bottomBar = {
                                NavigationBar {
                                    NavigationBarItem(
                                        selected = currentRoute == "orders",
                                        onClick = { homeNavController.navigate("orders") },
                                        icon = { Icon(Icons.Default.ShoppingCart, contentDescription = null) },
                                        label = { Text("Commandes") }
                                    )
                                    // Onglet Produits visible uniquement pour ADMIN
                                    if (userRole == "ADMIN") {
                                        NavigationBarItem(
                                            selected = currentRoute == "products",
                                            onClick = { homeNavController.navigate("products") },
                                            icon = { Icon(Icons.Default.List, contentDescription = null) },
                                            label = { Text("Produits") }
                                        )
                                    }
                                }
                            }
                        ) { innerPadding ->
                            NavHost(
                                navController = homeNavController,
                                startDestination = "orders",
                                modifier = Modifier.padding(innerPadding)
                            ) {
                                composable("orders") {
                                    OrderScreen(modifier = Modifier.fillMaxSize())
                                }
                                composable("products") {
                                    val productViewModel: ProductViewModel = viewModel(
                                        factory = ProductViewModelFactory(authToken)
                                    )
                                    ProductListScreen(
                                        modifier = Modifier.fillMaxSize(),
                                        viewModel = productViewModel
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}