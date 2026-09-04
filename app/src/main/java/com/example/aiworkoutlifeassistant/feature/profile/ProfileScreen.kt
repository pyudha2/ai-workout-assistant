package com.example.aiworkoutlifeassistant.feature.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth

@Composable
fun ProfileScreen(){
    val user = FirebaseAuth.getInstance().currentUser
    ProfileScreenContent(
        name = user?.displayName ?: "Nama belum diatur",
        email = user?.email ?: "-"
    )
}

@Composable
fun ProfileScreenContent(name: String, email: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(text = name)
        Text(text = email)
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileScreenContentPreview(){
    ProfileScreenContent(name = "Jhon Doe", email = "jhondoe@example.com")
}