package com.example.aiworkoutlifeassistant.feature.workout.domain.usecase

import com.example.aiworkoutlifeassistant.core.utils.Resource
import com.example.aiworkoutlifeassistant.feature.workout.domain.repository.WorkoutRepository
import javax.inject.Inject

class SendMessageUseCase @Inject constructor(
    private val repository: WorkoutRepository
){
    suspend operator fun invoke(uid: String, prompt: String): Resource<String> =
        repository.sendMessage(uid,prompt)
}