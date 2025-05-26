package com.example.sehatsehat.ui.customer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


class ChatbotActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

        }
    }

    @Composable
    fun ChatScreen(messages: List<String>) {
        Column(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier.weight(1f),
                reverseLayout = true // Ensures new messages appear at the bottom
            ) {
                items(messages.size) { message ->
                    ChatBubble(message)
                }
            }
            MessageInput()
        }
    }

    @Composable
    fun ChatBubble(message: String) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
        ) {
            Text(text = message, modifier = Modifier.padding(8.dp))
        }
    }

    @Composable
    fun MessageInput() {
        var text by remember { mutableStateOf("") }
        TextField(
            value = text,
            onValueChange = { text = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Type a message...") }
        )
    }

}
