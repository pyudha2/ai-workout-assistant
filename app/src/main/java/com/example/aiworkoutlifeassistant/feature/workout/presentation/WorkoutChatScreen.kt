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
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.aiworkoutlifeassistant.feature.workout.presentation.components.ChatBubble
import com.example.aiworkoutlifeassistant.feature.workout.presentation.components.ChatInputBar
import com.example.aiworkoutlifeassistant.feature.workout.domain.model.ChatMessage

@Composable
fun WorkoutChatScreen(
    viewModel: WorkoutChatViewModel = hiltViewModel()
) {
    val messages by viewModel.messages.collectAsState()
    var input by remember { mutableStateOf("") }

    WorkoutChatContent(
        messages = messages,
        input = input,
        onInputChange = { input = it },
        onSend = {
            if (input.isNotBlank()) {
                viewModel.sendMessage(input)
                input = ""
            }
        }
    )
}

@Composable
fun WorkoutChatContent(
    messages: List<ChatMessage>,
    input: String,
    onInputChange: (String) -> Unit,
    onSend: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(messages) { chatMessage ->
                ChatBubble(chatMessage)
            }
        }
        ChatInputBar(
            text = input,
            onTextChange = onInputChange,
            onSend = onSend
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun WorkoutChatContentPreview() {
    WorkoutChatContent(
        messages = listOf(
            ChatMessage(role = "user", text = "Gimana form squat gw?", timestamp = 0L),
            ChatMessage(role = "assistant", text = "Coba jaga lutut sejajar sama ujung kaki ya.", timestamp = 0L)
        ),
        input = "",
        onInputChange = {},
        onSend = {}
    )
}