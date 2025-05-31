package com.example.sehatsehat.data.sources.remote

import com.example.sehatsehat.data.sources.local.ChatLogEntity

data class ChatBotMessage(
    val response:String
)
data class ChatLogDRO(
    val status:Int,
    val message:String,
)
data class ChatLogFromGroupDRO(
    val status:Int,
    val chats:List<ChatLogEntity>
)