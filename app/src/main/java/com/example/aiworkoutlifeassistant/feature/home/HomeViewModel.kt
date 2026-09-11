package com.example.aiworkoutlifeassistant.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aiworkoutlifeassistant.feature.auth.domain.repository.AuthRepository
import com.example.aiworkoutlifeassistant.feature.workout.domain.model.ChatMessage
import com.example.aiworkoutlifeassistant.feature.workout.domain.usecase.GetChatHistoryUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    getChatHistoryUseCase: GetChatHistoryUseCase
) : ViewModel() {

    val lastChatMessage: StateFlow<ChatMessage?> = FirebaseAuth.getInstance().currentUser?.uid
        ?.let { uid ->
            getChatHistoryUseCase(uid).map { it.lastOrNull() }
        }
        ?.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
        ?: kotlinx.coroutines.flow.MutableStateFlow(null)

    fun logout() {
        authRepository.logout()
    }
}