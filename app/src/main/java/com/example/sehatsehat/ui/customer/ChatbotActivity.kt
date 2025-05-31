package com.example.sehatsehat.ui.customer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.copy
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.text
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import com.example.sehatsehat.data.sources.local.ChatLogEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.UUID
import kotlin.text.format
import kotlin.text.isNotBlank


class ChatbotActivity : ComponentActivity() {
    val activeUser = "UserA"
    val dummyChatLogsList: List<ChatLogEntity> = listOf(
        ChatLogEntity(
            id = UUID.randomUUID().toString(),
            chat_group_id = "general_chat_01",
            username = "UserA",
            content = "Hey everyone! What's up?",
            createdAt = Date().time - (10 * 60 * 1000L), // 10 minutes ago
            updatedAt = Date().time - (10 * 60 * 1000L)
        ),
        ChatLogEntity(
            id = UUID.randomUUID().toString(),
            chat_group_id = "general_chat_01",
            username = "UserB",
            content = "Not much, just chilling. You?",
            createdAt = Date().time - (8 * 60 * 1000L), // 8 minutes ago
            updatedAt = Date().time - (8 * 60 * 1000L)
        ),
        ChatLogEntity(
            id = UUID.randomUUID().toString(),
            chat_group_id = "project_alpha_discussion",
            username = "UserC",
            content = "Anyone made progress on task #123?",
            createdAt = Date().time - (15 * 60 * 1000L), // 15 minutes ago
            updatedAt = Date().time - (15 * 60 * 1000L)
        ),
        ChatLogEntity(
            id = UUID.randomUUID().toString(),
            chat_group_id = "general_chat_01",
            username = "UserA",
            content = "Just finished a big report. Feeling relieved!",
            createdAt = Date().time - (5 * 60 * 1000L), // 5 minutes ago
            updatedAt = Date().time - (5 * 60 * 1000L)
        ),
        ChatLogEntity(
            id = UUID.randomUUID().toString(),
            chat_group_id = "project_alpha_discussion",
            username = "UserD",
            content = "Yes, I pushed some updates for task #123 a few minutes ago.",
            createdAt = Date().time - (3 * 60 * 1000L), // 3 minutes ago
            updatedAt = Date().time - (2 * 60 * 1000L) // Edited 2 minutes ago
        ),
        ChatLogEntity(
            id = UUID.randomUUID().toString(),
            chat_group_id = "random_talk",
            username = "UserE",
            content = "This message is marked as deleted.",
            createdAt = Date().time - (30 * 60 * 1000L), // 30 minutes ago
            updatedAt = Date().time - (28 * 60 * 1000L), // 28 minutes ago
            deletedAt = Date().time - (25 * 60 * 1000L)  // Deleted 25 minutes ago
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChatScreen(messages = dummyChatLogsList) {}

        }
    }

    @Composable
    fun ChatScreen(messages: List<ChatLogEntity>, onSendMessage: (String) -> Unit) {
        Column(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier.weight(1f),
                reverseLayout = true // Ensures new messages appear at the bottom
            ) {
                if (messages.isEmpty()) {
                    item {
                        Text(
                            text = "No messages yet...",
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                } else {
                    items(
                        messages.size
                    ) { message -> // 'message' here is a ChatLogEntity
                        ChatBubble(chatEntry = messages.get(message),
                            isCurrentUserMessage = messages.get(message).username == activeUser)
                    }
                }
            }
            MessageInput(onSendMessage) // Pass a callback for handling new messages
        }
    }

    @Composable
    fun MessageInput(onSendMessage: (String) -> Unit) {
        var text by remember { mutableStateOf(TextFieldValue("")) }

        TextField(
            value = text,
            onValueChange = {newText -> text = newText} ,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Enter a message") },
            trailingIcon = {
                IconButton(onClick = {
                    val currentText = text.text
                    if (currentText.isNotBlank()) {
                        onSendMessage(currentText)
                        text = TextFieldValue("") // Clear the text field
                    }
                }) {
                    Icon(
                        imageVector = Icons.Filled.Send,
                        contentDescription = "Send message"
                    )
                }
            }
        )
    }

 // Or "UserA" from your dummy data

    @Composable
    fun ChatBubble(
        chatEntry: ChatLogEntity,
        isCurrentUserMessage: Boolean // To determine if the message is from the current user
    ) {
        val bubbleColor = if (isCurrentUserMessage) {
            MaterialTheme.colorScheme.primaryContainer // Color for sent messages
        } else {
            MaterialTheme.colorScheme.secondaryContainer // Color for received messages
        }

        val bubbleAlignment = if (isCurrentUserMessage) {
            Alignment.End
        } else {
            Alignment.Start
        }

        // Define corner radii for the bubble shape
        // This creates a more "tailed" look by having one corner less rounded
        val bubbleShape = if (isCurrentUserMessage) {
            RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 4.dp, // Less rounded for the "tail" side
                bottomStart = 16.dp,
                bottomEnd = 16.dp
            )
        } else {
            RoundedCornerShape(
                topStart = 4.dp, // Less rounded for the "tail" side
                topEnd = 16.dp,
                bottomStart = 16.dp,
                bottomEnd = 16.dp
            )
        }

        // Formatting the timestamp
        val sdf = SimpleDateFormat("hh:mm a", java.util.Locale.getDefault())
        val timeString = sdf.format(Date(chatEntry.createdAt))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalAlignment = bubbleAlignment // Align the whole bubble column
        ) {
            // Optional: Display username for received messages
            if (!isCurrentUserMessage) {
                Text(
                    text = chatEntry.username,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(start = if (isCurrentUserMessage) 0.dp else 8.dp, bottom = 2.dp)
                )
            }

            Surface( // Using Surface for elevation and shape
                shape = bubbleShape,
                color = bubbleColor,
                modifier = Modifier
                    .wrapContentSize() // Bubble should only be as big as its content
                    .widthIn(max = 300.dp) // Max width for the bubble
            ) {
                Column( // Use column if you want text and timestamp inside the bubble
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = chatEntry.content,
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (isCurrentUserMessage) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSecondaryContainer
                    )
                    Text(
                        text = timeString,
                        style = MaterialTheme.typography.labelSmall,
                        color = (if (isCurrentUserMessage) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSecondaryContainer).copy(alpha = 0.7f),
                        modifier = Modifier
                            .align(Alignment.End) // Timestamp aligned to the end of the text column
                            .padding(top = 4.dp)
                    )
                }
            }
        }
    }


}
