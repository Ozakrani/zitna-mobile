package com.example.zitnamobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.zitnamobile.data.remote.RetrofitClient
import com.example.zitnamobile.data.repository.AuthRepositoryImpl
import com.example.zitnamobile.ui.OrderScreen
import com.example.zitnamobile.ui.auth.AuthViewModel
import com.example.zitnamobile.ui.auth.AuthViewModelFactory
import com.example.zitnamobile.ui.auth.LoginScreen
import com.example.zitnamobile.ui.auth.RegisterScreen
import com.example.zitnamobile.ui.theme.ZitnaMobileTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val repository = AuthRepositoryImpl(RetrofitClient.apiService)
        val factory = AuthViewModelFactory(repository)

        setContent {
            ZitnaMobileTheme {
                val navController = rememberNavController()
                val viewModel: AuthViewModel = viewModel(factory = factory)

                NavHost(navController = navController, startDestination = "login") {
                    composable("login") {
                        LoginScreen(
                            viewModel = viewModel,
                            onLoginSuccess = {
                                navController.navigate("home") {
                                    popUpTo("login") { inclusive = true }
                                }
                            },
                            onNavigateToRegister = { navController.navigate("register") }
                        )
                    }
                    composable("register") {
                        RegisterScreen(
                            viewModel = viewModel,
                            onRegisterSuccess = {
                                navController.navigate("home") {
                                    popUpTo("register") { inclusive = true }
                                }
                            },
                            onNavigateToLogin = { navController.popBackStack() }
                        )
                    }
                    composable("home") {
                        OrderScreen(modifier = Modifier.fillMaxSize())
                    }
                }
            }
        }
    }
}