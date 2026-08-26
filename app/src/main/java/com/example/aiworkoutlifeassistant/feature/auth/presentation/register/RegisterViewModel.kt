package com.example.aiworkoutlifeassistant.feature.auth.presentation.register

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
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _registerState = MutableStateFlow<Resource<User>?>(null)
    val registerState: StateFlow<Resource<User>?> = _registerState.asStateFlow()

    fun register(name: String, email: String, password: String, confirmPassword: String){
        if (name.isBlank() || email.isBlank() || password.isBlank()){
            _registerState.value = Resource.Error("Semua Kolom Wajib Diisi")
            return
        }
        if(password != confirmPassword){
            _registerState.value = Resource.Error("Password Tidak Sama")
            return
        }
        viewModelScope.launch {
            authRepository.register(name, email, password).collect {
                result -> _registerState.value = result
            }
        }
    }
}