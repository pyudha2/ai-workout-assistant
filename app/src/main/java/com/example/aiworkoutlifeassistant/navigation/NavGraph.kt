package com.example.aiworkoutlifeassistant.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.aiworkoutlifeassistant.feature.auth.presentation.register.RegisterScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Register.route
    ) {
        composable(Screen.Register.route) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Login.route) {
            // placeholder, nanti diisi pas LoginScreen dibikin
            Text("Login Screen")
        }

        composable(Screen.Home.route) {
            // placeholder, nanti diisi pas Home Screen dibikin
            Text("Home Screen")
        }
    }
}