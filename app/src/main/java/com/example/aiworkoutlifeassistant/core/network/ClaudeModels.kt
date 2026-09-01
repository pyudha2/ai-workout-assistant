package com.example.aiworkoutlifeassistant.core.network

import kotlinx.serialization.Serializable

@Serializable
data class ClaudeRequest(
    val model: String,
    val max_tokens: Int,
    val message: List<ClaudeMessage>
)

@Serializable
data class ClaudeMessage(
    val role: String,
    val content: String
)

@Serializable
data class ClaudeResponse(
    val content: List<ClaudeContentBlock>
)

@Serializable
data class ClaudeContentBlock(
    val type: String,
    val text: String
)