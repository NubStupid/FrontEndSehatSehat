package com.example.sehatsehat.ui.customer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.sehatsehat.data.sources.local.ChatLogEntity


class ChatbotActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

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
                        ChatBubble(messages.get(it))
                    }
                }
            }
            MessageInput(onSendMessage) // Pass a callback for handling new messages
        }
    }

    @Composable
    fun MessageInput(onSendMessage: (String) -> Unit) {
        var text by remember { mutableStateOf("") }

        TextField(
            TextFieldValue(""),
            value = text,
            onValueChange = { text = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { "Type a message..." },
            onImeActionPerformed = { action, controller ->
                if (action == ImeAction.Done) {
                    onSendMessage(text)
                    text = "" // Reset input after sending
                    controller?.hideSoftwareKeyboard()
                }
            }
        )
    }


    @Composable
    fun ChatBubble(chatEntry: ChatLogEntity) { // Parameter type changed
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
        ) {
            Text(
                text = chatEntry.content, // Accessing and displaying the 'content' field
                modifier = Modifier.padding(8.dp)
            )
        }
    }


}
