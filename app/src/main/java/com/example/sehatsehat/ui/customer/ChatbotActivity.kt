package com.example.sehatsehat.ui.customer

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.sehatsehat.SehatViewModelFactory
import com.example.sehatsehat.model.ChatLogEntity
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.text.isNotBlank
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.text.style.TextOverflow
import com.example.sehatsehat.viewmodel.ChatbotViewModel

class ChatbotActivity : ComponentActivity() {
    val vm by viewModels<ChatbotViewModel>(){SehatViewModelFactory}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm.init("john_doe", "john_doe_chatbot")
        enableEdgeToEdge()
        setContent {
            ChatScreen(messages = vm.chatMessages.value ?: emptyList()) {}

        }
    }

    @Composable
    fun ChatScreen(messages: List<ChatLogEntity>, onSendMessage: (String) -> Unit) {
        val chatMessages by vm.chatMessages.observeAsState(emptyList())
        LaunchedEffect(key1 = Unit) {
            Log.d("DQWD",vm.chatGroup.value!!)
            vm.chatbotSync(vm.chatGroup.value!!)
        }
        Scaffold(
            topBar = {
                ChatbotTopAppBar(
                    onNavigationIconClick = {}
                )
            }
        ) { innerPadding ->
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)) {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    reverseLayout = true // Ensures new messages appear at the bottom
                ) {
                    if (chatMessages.isEmpty()) {
                        item {
                            Text(
                                text = "No messages yet...",
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    } else {
                        items(
                            chatMessages.size
                        ) { message -> // 'message' here is a ChatLogEntity
                            ChatBubble(chatEntry = chatMessages.get(message),
                                isCurrentUserMessage = chatMessages.get(message).username == vm.username.value)
                        }
                    }
                }
                MessageInput(onSendMessage) // Pass a callback for handling new messages
            }
        }
    }

    @Composable
    fun MessageInput(onSendMessage: (String) -> Unit) {
        var text by remember { mutableStateOf(TextFieldValue("")) }

        TextField(
            value = text,
            onValueChange = {newText ->
                text = newText
                vm.updateMessage(newText.text)
                            } ,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Enter a message") },
            trailingIcon = {
                IconButton(onClick = {
                    val currentText = text.text
                    if (currentText.isNotBlank()) {
                        onSendMessage(currentText)
                        vm.sendMessages()
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

    @OptIn(ExperimentalMaterial3Api::class) // CenterAlignedTopAppBar is experimental
    @Composable
    fun ChatbotTopAppBar(
        modifier: Modifier = Modifier,
        onNavigationIconClick: (() -> Unit)? = null, // Optional: For a back arrow or menu
        onActionIconClick: (() -> Unit)? = null      // Optional: For an action icon like settings
    ) {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    "Chatbot",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleLarge // Or any other style you prefer
                )
            },
            modifier = modifier,
            navigationIcon = {
                onNavigationIconClick?.let { onClick ->
                    IconButton(onClick = onClick) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack, // Or Icons.Filled.Menu
                            contentDescription = "Navigation Icon" // "Back" or "Open navigation menu"
                        )
                    }
                }
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary, // Example: Blue background
                titleContentColor = MaterialTheme.colorScheme.onPrimary, // Example: White text
                navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                actionIconContentColor = MaterialTheme.colorScheme.onPrimary
            )
            // You can also customize scrollBehavior if needed
            // scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
        )
    }
    override fun onResume() {
        super.onResume()
        vm.chatbotSync("GC00001")
    }

}
