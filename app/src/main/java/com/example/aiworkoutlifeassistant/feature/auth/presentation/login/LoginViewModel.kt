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
}