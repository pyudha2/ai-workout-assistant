package com.example.aiworkoutlifeassistant.core.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

enum class DrawerDestination { HOME, PROFILE, SETTINGS }

@Composable
fun AppDrawerContent(
    currentDestination: DrawerDestination,
    onHomeClick: () -> Unit,
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
                label = { Text("Home") },
                icon = { Icon(Icons.Filled.Home, contentDescription = null) },
                selected = currentDestination == DrawerDestination.HOME,
                onClick = onHomeClick,
                modifier = Modifier.padding(horizontal = 12.dp)
            )
            NavigationDrawerItem(
                label = { Text("Profile") },
                icon = { Icon(Icons.Filled.AccountCircle, contentDescription = null) },
                selected = currentDestination == DrawerDestination.PROFILE,
                onClick = onProfileClick,
                modifier = Modifier.padding(horizontal = 12.dp)
            )
            NavigationDrawerItem(
                label = { Text("Settings") },
                icon = { Icon(Icons.Filled.Settings, contentDescription = null) },
                selected = currentDestination == DrawerDestination.SETTINGS,
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

@Preview(showBackground = true)
@Composable
private fun AppDrawerContentPreview() {
    AppDrawerContent(
        currentDestination = DrawerDestination.HOME,
        onHomeClick = {},
        onProfileClick = {},
        onSettingsClick = {},
        onLogoutClick = {}
    )
}