package com.example.aiworkoutlifeassistant.feature.auth.domain.repository

import com.example.aiworkoutlifeassistant.core.utils.Resource
import com.example.aiworkoutlifeassistant.feature.auth.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository{
    fun register(name: String, email: String, password: String): Flow<Resource<User>>
}