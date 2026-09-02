package com.example.aiworkoutlifeassistant.feature.workout.data.model

import com.example.aiworkoutlifeassistant.feature.workout.domain.model.ChatMessage

data class ChatMessageEntity (
    val role: String = "",
    val text: String = "",
    val timestamp: Long = 0L
){
    fun toDomain() = ChatMessage(role, text, timestamp)

    companion object{
        fun fromDomain(message: ChatMessage) = ChatMessageEntity(
            message.role,
            message.text,
            message.timestamp
        )
    }
}