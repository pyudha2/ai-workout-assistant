package com.example.aiworkoutlifeassistant.feature.auth.presentation

import androidx.lifecycle.ViewModel
import com.example.aiworkoutlifeassistant.feature.auth.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    fun logout() {
        authRepository.logout()
    }
}