package com.example.aiworkoutlifeassistant.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun HomeScreen(
    onNavigateToWorkoutChat: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onLogout: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    HomeScreenContent(
        onWorkoutChatClick = onNavigateToWorkoutChat,
        onProfileClick = onNavigateToProfile,
        onSettingsClick = onNavigateToSettings,
        onLogoutClick = {
            viewModel.logout()
            onLogout()
        }
    )
}

@Composable
fun HomeScreenContent(
    onWorkoutChatClick: () -> Unit,
    onProfileClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Belum ada sesi workout minggu ini",
            style = MaterialTheme.typography.bodyLarge
        )
        Button(onClick = onWorkoutChatClick, modifier = Modifier.fillMaxWidth()) {
            Text("Workout Chat")
        }
        OutlinedButton(onClick = onProfileClick, modifier = Modifier.fillMaxWidth()) {
            Text("Profile")
        }
        OutlinedButton(onClick = onSettingsClick, modifier = Modifier.fillMaxWidth()) {
            Text("Settings")
        }
        OutlinedButton(onClick = onLogoutClick, modifier = Modifier.fillMaxWidth()) {
            Text("Logout")
        }
    }
}