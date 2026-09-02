package com.example.aiworkoutlifeassistant.feature.workout.domain.model

data class ChatMessage(
    val role: String,
    val text: String,
    val timestamp: Long
)