package com.example.aiworkoutlifeassistant.feature.workout.data.repository

import com.example.aiworkoutlifeassistant.BuildConfig
import com.example.aiworkoutlifeassistant.core.data.firebase.FirestoreService
import com.example.aiworkoutlifeassistant.core.network.AnthropicService
import com.example.aiworkoutlifeassistant.core.network.ClaudeMessage
import com.example.aiworkoutlifeassistant.core.network.ClaudeRequest
import com.example.aiworkoutlifeassistant.core.utils.Constants
import com.example.aiworkoutlifeassistant.core.utils.Resource
import com.example.aiworkoutlifeassistant.feature.workout.data.model.ChatMessageEntity
import com.example.aiworkoutlifeassistant.feature.workout.domain.model.ChatMessage
import com.example.aiworkoutlifeassistant.feature.workout.domain.repository.WorkoutRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class WorkoutRepositoryImpl @Inject constructor(
    private val anthropicService: AnthropicService,
    private val firestoreService: FirestoreService
) : WorkoutRepository {
    override suspend fun sendMessage(uid: String, prompt: String): Resource<String> {
        try {
            val userMessage = ChatMessage(role = "user", text = prompt, timestamp = System.currentTimeMillis())
            firestoreService.saveChatMessage(uid, ChatMessageEntity.fromDomain(userMessage))

            val response = anthropicService.sendMessage(
                apiKey = BuildConfig.CLAUDE_API_KEY,
                version = Constants.ANTHROPIC_VERSION,
                body = ClaudeRequest(
                    model = Constants.CLAUDE_MODEL,
                    max_tokens = Constants.MAX_TOKENS,
                    message = listOf(ClaudeMessage(role = "user", content = prompt))
                )
            )
            val replyText = response.content.firstOrNull()?.text.orEmpty()

            val assistantMessage = ChatMessage(role = "assistant", text = replyText, timestamp = System.currentTimeMillis())
            firestoreService.saveChatMessage(uid, ChatMessageEntity.fromDomain(assistantMessage))

            return Resource.Success(data = replyText)
        } catch (e: Exception){
            return Resource.Error(e.message ?: "Unknown error")
        }
    }

    override fun getChatHistory(uid: String): Flow<List<ChatMessage>> =
        firestoreService.getChatHistory(uid).map {
            list ->
            return@map list.map { it.toDomain() }
        }
}