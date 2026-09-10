package com.example.aiworkoutlifeassistant.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.aiworkoutlifeassistant.core.presentation.components.AppDrawerContent
import com.example.aiworkoutlifeassistant.core.presentation.components.AppTopBar
import com.example.aiworkoutlifeassistant.core.presentation.components.DrawerDestination
import com.example.aiworkoutlifeassistant.feature.workout.domain.model.ChatMessage
import kotlinx.coroutines.launch
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
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawerContent(
                currentDestination = DrawerDestination.HOME,
                onHomeClick = { scope.launch { drawerState.close() } },
                onProfileClick = {
                    scope.launch { drawerState.close() }
                    onNavigateToProfile()
                },
                onSettingsClick = {
                    scope.launch { drawerState.close() }
                    onNavigateToSettings()
                },
                onLogoutClick = {
                    scope.launch { drawerState.close() }
                    onLogout()
                }
            )
        }
    ) {
        HomeScreenContent(
            summaryText = formatSummary(lastMessage),
            onMenuClick = { scope.launch { drawerState.open() } },
            onWorkoutChatClick = onNavigateToWorkoutChat
        )
    }
}

private fun formatSummary(lastMessage: ChatMessage?): String {
    if (lastMessage == null) return "Belum ada sesi workout"
    val formatter = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale("id", "ID"))
    return "Terakhir chat: ${formatter.format(Date(lastMessage.timestamp))}"
}

@Composable
fun HomeScreenContent(
    summaryText: String,
    onMenuClick: () -> Unit,
    onWorkoutChatClick: () -> Unit
) {
    Scaffold(
        topBar = {
            AppTopBar(onMenuClick = onMenuClick)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
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
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenContentPreview() {
    HomeScreenContent(
        summaryText = "Terakhir chat: 04 Sep 2026, 14:30",
        onMenuClick = {},
        onWorkoutChatClick = {}
    )
}