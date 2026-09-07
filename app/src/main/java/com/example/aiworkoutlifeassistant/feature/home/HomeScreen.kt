package com.example.aiworkoutlifeassistant.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.aiworkoutlifeassistant.feature.workout.domain.model.ChatMessage
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
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
            HomeDrawerContent(
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
                    viewModel.logout()
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
fun HomeDrawerContent(
    onProfileClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    ModalDrawerSheet {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 16.dp)
        ) {
            NavigationDrawerItem(
                label = { Text("Profile") },
                icon = { Icon(Icons.Filled.AccountCircle, contentDescription = null) },
                selected = false,
                onClick = onProfileClick,
                modifier = Modifier.padding(horizontal = 12.dp)
            )
            NavigationDrawerItem(
                label = { Text("Settings") },
                icon = { Icon(Icons.Filled.Settings, contentDescription = null) },
                selected = false,
                onClick = onSettingsClick,
                modifier = Modifier.padding(horizontal = 12.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            NavigationDrawerItem(
                label = { Text("Logout") },
                icon = { Icon(Icons.Filled.ExitToApp, contentDescription = null) },
                selected = false,
                onClick = onLogoutClick,
                modifier = Modifier.padding(horizontal = 12.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenContent(
    summaryText: String,
    onMenuClick: () -> Unit,
    onWorkoutChatClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Home") },
                navigationIcon = {
                    IconButton(onClick = onMenuClick) {
                        Icon(Icons.Filled.Menu, contentDescription = "Menu")
                    }
                }
            )
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

@Preview(showBackground = true)
@Composable
private fun HomeDrawerContentPreview() {
    HomeDrawerContent(
        onProfileClick = {},
        onSettingsClick = {},
        onLogoutClick = {}
    )
}