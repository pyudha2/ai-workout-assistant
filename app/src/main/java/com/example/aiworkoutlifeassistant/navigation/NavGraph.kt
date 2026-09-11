package com.example.aiworkoutlifeassistant.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.aiworkoutlifeassistant.feature.auth.presentation.AuthViewModel
import com.example.aiworkoutlifeassistant.feature.auth.presentation.login.LoginScreen
import com.example.aiworkoutlifeassistant.feature.auth.presentation.register.RegisterScreen
import com.example.aiworkoutlifeassistant.feature.home.HomeScreen
import com.example.aiworkoutlifeassistant.feature.profile.ProfileScreen
import com.example.aiworkoutlifeassistant.feature.settings.presentation.SettingsScreen
import com.example.aiworkoutlifeassistant.feature.workout.presentation.WorkoutChatScreen
import com.google.firebase.auth.FirebaseAuth

@Composable
fun NavGraph(navController: NavHostController = rememberNavController()) {
    val isLoggedIn = FirebaseAuth.getInstance().currentUser != null
    val startDestination = if (isLoggedIn) Screen.Home.route else Screen.Login.route
    val authViewModel: AuthViewModel = hiltViewModel()

    val onLogout: () -> Unit = {
        authViewModel.logout()
        navController.navigate(Screen.Login.route) {
            popUpTo(0) { inclusive = true }
        }
    }

    NavHost(navController = navController, startDestination = startDestination) {
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = { navController.navigate(Screen.Register.route) }
            )
        }
        composable(Screen.Register.route) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToLogin = { navController.popBackStack() }
            )
        }
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToWorkoutChat = { navController.navigate(Screen.WorkoutChat.route) },
                onNavigateToProfile = {
                    navController.navigate(Screen.Profile.route) {
                        launchSingleTop = true
                    }
                },
                onNavigateToSettings = {
                    navController.navigate(Screen.Settings.route) {
                        launchSingleTop = true
                    }
                },
                onLogout = onLogout
            )
        }
        composable(Screen.WorkoutChat.route) {
            WorkoutChatScreen()
        }
        composable(Screen.Profile.route) {
            ProfileScreen(
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                },
                onNavigateToSettings = {
                    navController.navigate(Screen.Settings.route) {
                        launchSingleTop = true
                    }
                },
                onLogout = onLogout
            )
        }
        composable(Screen.Settings.route) {
            SettingsScreen(
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                },
                onNavigateToProfile = {
                    navController.navigate(Screen.Profile.route) {
                        launchSingleTop = true
                    }
                },
                onAccountDeleted = onLogout,
                onLogout = onLogout
            )
        }
    }
}