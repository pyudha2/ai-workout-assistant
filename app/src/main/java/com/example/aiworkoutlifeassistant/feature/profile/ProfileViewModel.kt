package com.example.aiworkoutlifeassistant.feature.profile

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import javax.inject.Inject

sealed class ProfilePhotoState {
    object Idle : ProfilePhotoState()
    object Uploading : ProfilePhotoState()
    data class Error(val message: String) : ProfilePhotoState()
}

@HiltViewModel
class ProfileViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : ViewModel() {

    private val _photoBase64 = MutableStateFlow<String?>(null)
    val photoBase64: StateFlow<String?> = _photoBase64

    private val _uploadState = MutableStateFlow<ProfilePhotoState>(ProfilePhotoState.Idle)
    val uploadState: StateFlow<ProfilePhotoState> = _uploadState

    val name: String
        get() = auth.currentUser?.displayName ?: "Nama belum diatur"

    val email: String
        get() = auth.currentUser?.email ?: "-"

    init {
        loadPhoto()
    }

    private fun loadPhoto() {
        val uid = auth.currentUser?.uid ?: return
        viewModelScope.launch {
            try {
                val snapshot = firestore.collection("users").document(uid).get().await()
                _photoBase64.value = snapshot.getString("photoBase64")
            } catch (e: Exception) {
                // gagal load foto lama bukan error fatal, biarin null (tampil placeholder)
            }
        }
    }

    fun uploadPhoto(uri: Uri) {
        val uid = auth.currentUser?.uid ?: return
        _uploadState.value = ProfilePhotoState.Uploading
        viewModelScope.launch {
            try {
                val base64 = withContext(Dispatchers.Default) { compressToBase64(uri) }
                firestore.collection("users").document(uid)
                    .set(mapOf("photoBase64" to base64), SetOptions.merge())
                    .await()
                _photoBase64.value = base64
                _uploadState.value = ProfilePhotoState.Idle
            } catch (e: Exception) {
                _uploadState.value = ProfilePhotoState.Error(e.message ?: "Gagal upload foto")
            }
        }
    }

    private fun compressToBase64(uri: Uri): String {
        val inputStream = context.contentResolver.openInputStream(uri)
        val original = BitmapFactory.decodeStream(inputStream)
        inputStream?.close()

        val maxDimension = 300
        val ratio = minOf(
            maxDimension.toFloat() / original.width,
            maxDimension.toFloat() / original.height
        )
        val resized = if (ratio < 1f) {
            Bitmap.createScaledBitmap(
                original,
                (original.width * ratio).toInt(),
                (original.height * ratio).toInt(),
                true
            )
        } else {
            original
        }

        val outputStream = ByteArrayOutputStream()
        resized.compress(Bitmap.CompressFormat.JPEG, 60, outputStream)
        return Base64.encodeToString(outputStream.toByteArray(), Base64.NO_WRAP)
    }
}