package com.example.sehatsehat.data.sources.remote

import com.example.sehatsehat.data.sources.local.ChatLogEntity

data class ChatBotDTO(
    val message:String
)
data class ChatLogDTO(
    val content:String,
    val username:String,
    val chat_group:String
)

data class SyncChatGroupDTO(
    val group_id:String,
    val logs:List<ChatLogEntity>
)