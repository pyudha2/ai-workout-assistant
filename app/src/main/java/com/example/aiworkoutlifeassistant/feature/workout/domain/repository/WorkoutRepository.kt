package com.example.aiworkoutlifeassistant.feature.workout.domain.repository

import com.example.aiworkoutlifeassistant.core.utils.Resource
import com.example.aiworkoutlifeassistant.feature.workout.domain.model.ChatMessage
import kotlinx.coroutines.flow.Flow

interface WorkoutRepository{
    suspend fun sendMessage(uid: String, prompt: String): Resource<String>
    fun getChatHistory(uid: String): Flow<List<ChatMessage>>
}