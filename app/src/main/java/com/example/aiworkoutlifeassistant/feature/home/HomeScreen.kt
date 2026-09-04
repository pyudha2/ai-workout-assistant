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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.aiworkoutlifeassistant.feature.workout.domain.model.ChatMessage
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HomeScreen(
    onNavigateToWorkoutChat: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onLogout: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val lastMessage by viewModel.lastChatMessage.collectAsState()

    HomeScreenContent(
        summaryText = formatSummary(lastMessage),
        onWorkoutChatClick = onNavigateToWorkoutChat,
        onProfileClick = onNavigateToProfile,
        onSettingsClick = onNavigateToSettings,
        onLogoutClick = {
            viewModel.logout()
            onLogout()
        }
    )
}

private fun formatSummary(lastMessage: ChatMessage?): String {
    if (lastMessage == null) return "Belum ada sesi workout"
    val formatter = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale("id", "ID"))
    return "Terakhir chat: ${formatter.format(Date(lastMessage.timestamp))}"
}

@Composable
fun HomeScreenContent(
    summaryText: String,
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
            text = summaryText,
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

@Preview(showBackground = true)
@Composable
private fun HomeScreenContentPreview() {
    HomeScreenContent(
        summaryText = "Terakhir chat: 04 Sep 2026, 14:30",
        onWorkoutChatClick = {},
        onProfileClick = {},
        onSettingsClick = {},
        onLogoutClick = {}
    )
}