package com.example.aiworkoutlifeassistant.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.aiworkoutlifeassistant.feature.auth.presentation.login.LoginScreen
import com.example.aiworkoutlifeassistant.feature.auth.presentation.register.RegisterScreen
import com.example.aiworkoutlifeassistant.feature.home.HomeScreen
import com.google.firebase.auth.FirebaseAuth

@Composable
fun NavGraph(navController: NavHostController = rememberNavController()) {
    val isLoggedIn = FirebaseAuth.getInstance().currentUser != null
    val startDestination = if (isLoggedIn) Screen.Home.route else Screen.Login.route

    NavHost(navController = navController, startDestination = startDestination) {
        composable(Screen.Login.route) {
            LoginScreen(onNavigateToRegister = { navController.navigate(Screen.Register.route) })
        }
        composable(Screen.Register.route) {
            RegisterScreen(onNavigateToLogin = { navController.popBackStack() })
        }
        composable(Screen.Home.route) {
            HomeScreen(
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}