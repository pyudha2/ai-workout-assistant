package com.example.aiworkoutlifeassistant.feature.profile

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.aiworkoutlifeassistant.core.presentation.components.AppDrawerContent
import com.example.aiworkoutlifeassistant.core.presentation.components.AppTopBar
import com.example.aiworkoutlifeassistant.core.presentation.components.DrawerDestination
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(
    onNavigateToHome: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onLogout: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val photoBase64 by viewModel.photoBase64.collectAsState()
    val uploadState by viewModel.uploadState.collectAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { viewModel.uploadPhoto(it) }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawerContent(
                currentDestination = DrawerDestination.PROFILE,
                onHomeClick = {
                    scope.launch { drawerState.close() }
                    onNavigateToHome()
                },
                onProfileClick = { scope.launch { drawerState.close() } },
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
        ProfileScreenContent(
            name = viewModel.name,
            email = viewModel.email,
            photoBase64 = photoBase64,
            isUploading = uploadState is ProfilePhotoState.Uploading,
            errorMessage = (uploadState as? ProfilePhotoState.Error)?.message,
            onChangePhotoClick = { launcher.launch("image/*") },
            onMenuClick = { scope.launch { drawerState.open() } }
        )
    }
}

@Composable
fun ProfileScreenContent(
    name: String,
    email: String,
    photoBase64: String?,
    isUploading: Boolean,
    errorMessage: String?,
    onChangePhotoClick: () -> Unit,
    onMenuClick: () -> Unit
) {
    Scaffold(
        topBar = {
            AppTopBar(title = "Profile", onMenuClick = onMenuClick)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                val bitmap = remember(photoBase64) { decodeBase64ToBitmap(photoBase64) }
                if (bitmap != null) {
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = "Foto profil",
                        modifier = Modifier
                            .size(96.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        imageVector = Icons.Filled.AccountCircle,
                        contentDescription = "Foto profil",
                        modifier = Modifier.size(96.dp)
                    )
                }
                if (isUploading) {
                    CircularProgressIndicator()
                }
            }
            TextButton(onClick = onChangePhotoClick) {
                Text("Ganti Foto")
            }
            if (errorMessage != null) {
                Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = name)
            Text(text = email)
        }
    }
}

private fun decodeBase64ToBitmap(base64: String?): Bitmap? {
    if (base64.isNullOrEmpty()) return null
    return try {
        val bytes = Base64.decode(base64, Base64.NO_WRAP)
        BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    } catch (e: Exception) {
        null
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileScreenContentPreview() {
    ProfileScreenContent(
        name = "Jhon Doe",
        email = "jhondoe@example.com",
        photoBase64 = null,
        isUploading = false,
        errorMessage = null,
        onChangePhotoClick = {},
        onMenuClick = {}
    )
}