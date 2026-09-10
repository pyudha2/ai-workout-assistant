package com.example.aiworkoutlifeassistant.feature.profile

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

sealed class ProfilePhotoState {
    object Idle : ProfilePhotoState()
    object Uploading : ProfilePhotoState()
    data class Error(val message: String) : ProfilePhotoState()
}

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val storage: FirebaseStorage
) : ViewModel() {

    private val _photoUrl = MutableStateFlow(auth.currentUser?.photoUrl?.toString())
    val photoUrl: StateFlow<String?> = _photoUrl

    private val _uploadState = MutableStateFlow<ProfilePhotoState>(ProfilePhotoState.Idle)
    val uploadState: StateFlow<ProfilePhotoState> = _uploadState

    val name: String
        get() = auth.currentUser?.displayName ?: "Nama belum diatur"

    val email: String
        get() = auth.currentUser?.email ?: "-"

    fun uploadPhoto(uri: Uri) {
        val user = auth.currentUser ?: return
        _uploadState.value = ProfilePhotoState.Uploading
        viewModelScope.launch {
            try {
                val ref = storage.reference.child("profile_photos/${user.uid}.jpg")
                ref.putFile(uri).await()
                val downloadUrl = ref.downloadUrl.await()
                user.updateProfile(
                    userProfileChangeRequest { photoUri = downloadUrl }
                ).await()
                _photoUrl.value = downloadUrl.toString()
                _uploadState.value = ProfilePhotoState.Idle
            } catch (e: Exception) {
                _uploadState.value = ProfilePhotoState.Error(e.message ?: "Gagal upload foto")
            }
        }
    }
}