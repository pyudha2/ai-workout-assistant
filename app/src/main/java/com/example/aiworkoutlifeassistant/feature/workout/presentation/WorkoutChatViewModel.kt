package com.example.aiworkoutlifeassistant.feature.workout.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aiworkoutlifeassistant.core.utils.Resource
import com.example.aiworkoutlifeassistant.feature.workout.domain.model.ChatMessage
import com.example.aiworkoutlifeassistant.feature.workout.domain.usecase.GetChatHistoryUseCase
import com.example.aiworkoutlifeassistant.feature.workout.domain.usecase.SendMessageUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkoutChatViewModel @Inject constructor(
    private val sendMessageUseCase: SendMessageUseCase,
    private val getChatHistoryUseCase: GetChatHistoryUseCase,
    private val firebaseAuth: FirebaseAuth
): ViewModel(){
    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val uid: String? get() = firebaseAuth.currentUser?.uid

    init {
        uid?.let {
            id -> viewModelScope.launch {
                getChatHistoryUseCase(id).collect {
                    _messages.value = it
                }
            }
        }
    }

    fun sendMessage(prompt: String){
        val id = uid ?: return
        viewModelScope.launch {
            _isLoading.value = true
            sendMessageUseCase(id, prompt)
            _isLoading.value = false
        }
    }
}