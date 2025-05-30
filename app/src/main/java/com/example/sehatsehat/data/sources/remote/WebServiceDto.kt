package com.example.sehatsehat.data.sources.remote

data class ChatBotDTO(
    val message:String
)
data class ChatLogDTO(
    val content:String,
    val username:String,
    val chat_group:String
)