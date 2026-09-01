package com.example.aiworkoutlifeassistant.core.network

import com.google.android.gms.common.api.internal.ApiKey
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface AnthropicService{
    @POST("v1/message")
    suspend fun sendMessage(
        @Header("x-api-key") apiKey: String,
        @Header("anthropic-version") version: String,
        @Body body: ClaudeRequest
    ): ClaudeResponse
}