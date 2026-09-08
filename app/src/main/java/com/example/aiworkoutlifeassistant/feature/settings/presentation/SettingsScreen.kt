package com.example.aiworkoutlifeassistant.feature.settings.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.aiworkoutlifeassistant.core.presentation.components.AppTopBar

@Composable
fun SettingsScreen(
    onBackClick: () -> Unit,
    onAccountDeleted: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val isDarkMode by viewModel.isDarkMode.collectAsState()
    val notificationsEnabled by viewModel.notificationsEnabled.collectAsState()
    val changePasswordResult by viewModel.changePasswordResult.collectAsState()
    val deleteAccountResult by viewModel.deleteAccountResult.collectAsState()

    var showChangePasswordDialog by remember { mutableStateOf(false) }
    var showDeleteAccountDialog by remember { mutableStateOf(false) }

    LaunchedEffect(deleteAccountResult) {
        if (deleteAccountResult is SettingsActionResult.Success) {
            onAccountDeleted()
        }
    }

    SettingsScreenContent(
        isDarkMode = isDarkMode,
        notificationsEnabled = notificationsEnabled,
        onBackClick = onBackClick,
        onDarkModeChange = viewModel::setDarkMode,
        onNotificationsChange = viewModel::setNotificationsEnabled,
        onChangePasswordClick = { showChangePasswordDialog = true },
        onDeleteAccountClick = { showDeleteAccountDialog = true }
    )

    if (showChangePasswordDialog) {
        ChangePasswordDialog(
            result = changePasswordResult,
            onConfirm = { current, new -> viewModel.changePassword(current, new) },
            onDismiss = {
                showChangePasswordDialog = false
                viewModel.resetChangePasswordResult()
            }
        )
    }

    if (showDeleteAccountDialog) {
        DeleteAccountDialog(
            result = deleteAccountResult,
            onConfirm = { password -> viewModel.deleteAccount(password) },
            onDismiss = {
                showDeleteAccountDialog = false
                viewModel.resetDeleteAccountResult()
            }
        )
    }
}

@Composable
fun SettingsScreenContent(
    isDarkMode: Boolean,
    notificationsEnabled: Boolean,
    onBackClick: () -> Unit,
    onDarkModeChange: (Boolean) -> Unit,
    onNotificationsChange: (Boolean) -> Unit,
    onChangePasswordClick: () -> Unit,
    onDeleteAccountClick: () -> Unit
) {
    Scaffold(
        topBar = {
            AppTopBar(title = "Settings", showBackButton = true, onBackClick = onBackClick)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Mode Gelap", style = MaterialTheme.typography.bodyLarge)
                Switch(checked = isDarkMode, onCheckedChange = onDarkModeChange)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Notifikasi", style = MaterialTheme.typography.bodyLarge)
                Switch(checked = notificationsEnabled, onCheckedChange = onNotificationsChange)
            }
            Spacer(modifier = Modifier.height(24.dp))
            OutlinedButton(onClick = onChangePasswordClick, modifier = Modifier.fillMaxWidth()) {
                Text("Ganti Password")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = onDeleteAccountClick, modifier = Modifier.fillMaxWidth()) {
                Text("Hapus Akun")
            }
        }
    }
}

@Composable
private fun ChangePasswordDialog(
    result: SettingsActionResult,
    onConfirm: (String, String) -> Unit,
    onDismiss: () -> Unit
) {
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }

    LaunchedEffect(result) {
        if (result is SettingsActionResult.Success) onDismiss()
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Ganti Password") },
        text = {
            Column {
                OutlinedTextField(
                    value = currentPassword,
                    onValueChange = { currentPassword = it },
                    label = { Text("Password saat ini") },
                    visualTransformation = PasswordVisualTransformation()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    label = { Text("Password baru") },
                    visualTransformation = PasswordVisualTransformation()
                )
                if (result is SettingsActionResult.Error) {
                    Text(result.message, color = MaterialTheme.colorScheme.error)
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(currentPassword, newPassword) }) {
                Text("Simpan")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Batal") }
        }
    )
}

@Composable
private fun DeleteAccountDialog(
    result: SettingsActionResult,
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var password by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Hapus Akun") },
        text = {
            Column {
                Text("Tindakan ini gak bisa dibatalin. Masukin password buat konfirmasi.")
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    visualTransformation = PasswordVisualTransformation()
                )
                if (result is SettingsActionResult.Error) {
                    Text(result.message, color = MaterialTheme.colorScheme.error)
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(password) }) {
                Text("Hapus")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Batal") }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun SettingsScreenContentPreview() {
    SettingsScreenContent(
        isDarkMode = false,
        notificationsEnabled = true,
        onBackClick = {},
        onDarkModeChange = {},
        onNotificationsChange = {},
        onChangePasswordClick = {},
        onDeleteAccountClick = {}
    )
}