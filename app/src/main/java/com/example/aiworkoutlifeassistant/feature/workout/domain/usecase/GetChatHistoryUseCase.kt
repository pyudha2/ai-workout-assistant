package com.example.aiworkoutlifeassistant.feature.workout.domain.usecase

import com.example.aiworkoutlifeassistant.feature.workout.domain.model.ChatMessage
import com.example.aiworkoutlifeassistant.feature.workout.domain.repository.WorkoutRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetChatHistoryUseCase @Inject constructor(
    private val repository: WorkoutRepository
){
    operator fun invoke(uid: String): Flow<List<ChatMessage>> =
        repository.getChatHistory(uid)
}