package com.example.aiworkoutlifeassistant.feature.home

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun HomeScreen(
    onLogout: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    HomeScreenContent(
        onLogoutClick = {
            viewModel.logout()
            onLogout()
        }
    )
}

@Composable
fun HomeScreenContent(onLogoutClick: () -> Unit) {
    Button(onClick = onLogoutClick) {
        Text("Logout")
    }
}