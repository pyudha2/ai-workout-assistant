package com.example.aiworkoutlifeassistant.feature.auth.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aiworkoutlifeassistant.core.utils.Resource
import com.example.aiworkoutlifeassistant.feature.auth.domain.model.User
import com.example.aiworkoutlifeassistant.feature.auth.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _loginState = MutableStateFlow<Resource<User>?>(null)
    val loginState: StateFlow<Resource<User>?> = _loginState.asStateFlow()

    private val _resetPasswordState = MutableStateFlow<Resource<Unit>?>(null)
    val resetPasswordState: StateFlow<Resource<Unit>?> = _resetPasswordState.asStateFlow()

    fun login(email: String, password: String){
        if (email.isBlank() || password.isBlank()){
            _loginState.value = Resource.Error("Semua Kolom Wajib Diisi")
            return
        }
        viewModelScope.launch {
            authRepository.login(email, password).collect {
                    result -> _loginState.value = result
            }
        }
    }

    fun resetPassword(email: String) {
        if (email.isBlank()) {
            _resetPasswordState.value = Resource.Error("Email wajib diisi")
            return
        }
        viewModelScope.launch {
            authRepository.resetPassword(email).collect { result ->
                _resetPasswordState.value = result
            }
        }
    }

    fun resetPasswordStateIdle() {
        _resetPasswordState.value = null
    }
}