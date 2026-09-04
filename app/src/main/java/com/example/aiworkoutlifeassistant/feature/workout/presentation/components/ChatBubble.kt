package com.example.aiworkoutlifeassistant.feature.workout.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.aiworkoutlifeassistant.feature.workout.domain.model.ChatMessage

@Composable
fun ChatBubble(message: ChatMessage){
    val isUser = message.role == "user"
    Row(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        Text(
            text = message.text,
            modifier = Modifier
                .background(
                    if (isUser) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                    RoundedCornerShape(12.dp)
                )
                .padding(12.dp),
            color = if (isUser) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ChatBubbleUserPreview(){
    ChatBubble(message = ChatMessage(role = "user", text = "Gimana form squat gw?", timestamp = 0L))
}

@Preview(showBackground = true)
@Composable
private fun ChatBubbleAssistantPreview(){
    ChatBubble(message = ChatMessage(role = "assistant", text = "Coba jaga lutut sejajar sama ujung kaki ya.", timestamp = 0L))
}