package com.example.aiworkoutlifeassistant.feature.settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aiworkoutlifeassistant.feature.settings.domain.repository.SettingsRepository
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class SettingsActionResult {
    object Idle : SettingsActionResult()
    object Loading : SettingsActionResult()
    object Success : SettingsActionResult()
    object RequiresRecentLogin : SettingsActionResult()
    data class Error(val message: String) : SettingsActionResult()
}

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel() {
    val isDarkMode: StateFlow<Boolean> = settingsRepository.isDarkMode
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val notificationsEnabled: StateFlow<Boolean> = settingsRepository.notificationsEnabled
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    private val _changePasswordResult = MutableStateFlow<SettingsActionResult>(SettingsActionResult.Idle)
    val changePasswordResult: StateFlow<SettingsActionResult> = _changePasswordResult

    private val _deleteAccountResult = MutableStateFlow<SettingsActionResult>(SettingsActionResult.Idle)
    val deleteAccountResult: StateFlow<SettingsActionResult> = _deleteAccountResult

    fun setDarkMode(enabled: Boolean) {
        viewModelScope.launch { settingsRepository.setDarkMode(enabled) }
    }

    fun setNotificationsEnabled(enabled: Boolean) {
        viewModelScope.launch { settingsRepository.setNotificationEnabled(enabled) }
    }

    fun changePassword(newPassword: String) {
        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            _changePasswordResult.value = SettingsActionResult.Error("User tidak ditemukan")
            return
        }

        _changePasswordResult.value = SettingsActionResult.Loading
        user.updatePassword(newPassword)
            .addOnSuccessListener {
                _changePasswordResult.value = SettingsActionResult.Success
            }
            .addOnFailureListener { exception ->
                _changePasswordResult.value = if (exception is FirebaseAuthRecentLoginRequiredException) {
                    SettingsActionResult.RequiresRecentLogin
                } else {
                    SettingsActionResult.Error(exception.message ?: "Gagal update password")
                }
            }
    }

    fun deleteAccount(currentPassword: String) {
        val user = FirebaseAuth.getInstance().currentUser
        val email = user?.email
        if (user == null || email == null) {
            _deleteAccountResult.value = SettingsActionResult.Error("User tidak ditemukan")
            return
        }
        _deleteAccountResult.value = SettingsActionResult.Loading
        val credential = EmailAuthProvider.getCredential(email, currentPassword)
        user.reauthenticate(credential)
            .addOnSuccessListener {
                user.delete()
                    .addOnSuccessListener { _deleteAccountResult.value = SettingsActionResult.Success }
                    .addOnFailureListener { _deleteAccountResult.value = SettingsActionResult.Error(it.message ?: "Gagal hapus akun") }
            }
            .addOnFailureListener {
                _deleteAccountResult.value = SettingsActionResult.Error("Password saat ini salah")
            }
    }

    fun resetChangePasswordResult() {
        _changePasswordResult.value = SettingsActionResult.Idle
    }

    fun resetDeleteAccountResult() {
        _deleteAccountResult.value = SettingsActionResult.Idle
    }
}