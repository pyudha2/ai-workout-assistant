package com.example.aiworkoutlifeassistant.feature.workout.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.aiworkoutlifeassistant.feature.workout.presentation.components.ChatBubble
import com.example.aiworkoutlifeassistant.feature.workout.presentation.components.ChatInputBar

@Composable
fun WorkoutChatScreen(
    viewModel: WorkoutChatViewModel = hiltViewModel()
) {
    val message by viewModel.messages.collectAsState()
    var input by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(message){ message ->
                ChatBubble(message)
            }
        }
        ChatInputBar(
            text = input,
            onTextChange = { input = it },
            onSend = {
                if (input.isNotBlank()) {
                    viewModel.sendMessage(input)
                    input = ""
                }
            }
        )
    }
}