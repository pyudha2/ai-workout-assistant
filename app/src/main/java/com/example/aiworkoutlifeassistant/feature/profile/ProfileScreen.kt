package com.example.aiworkoutlifeassistant.feature.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.aiworkoutlifeassistant.core.presentation.components.AppTopBar
import com.google.firebase.auth.FirebaseAuth
import org.jetbrains.annotations.Async

@Composable
fun ProfileScreen(onBackClick: () -> Unit){
    val user = FirebaseAuth.getInstance().currentUser
    ProfileScreenContent(
        name = user?.displayName ?: "Nama belum diatur",
        email = user?.email ?: "-",
        photoUrl = user?.photoUrl.toString(),
        onBackClick = onBackClick
    )
}

@Composable
fun ProfileScreenContent(name: String, email: String, photoUrl: String?, onBackClick: () -> Unit) {
    Scaffold(
        topBar = {
            AppTopBar (title = "Profile", showBackButton = true, onBackClick = onBackClick)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp)
        ) {
            AsyncImage(
                model = photoUrl,
                contentDescription = "Foto profil",
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Text(text = name)
            Text(text = email)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileScreenContentPreview(){
    ProfileScreenContent(
        name = "Jhon Doe",
        email = "jhondoe@example.com",
        photoUrl = null,
        onBackClick = {}
    )
}