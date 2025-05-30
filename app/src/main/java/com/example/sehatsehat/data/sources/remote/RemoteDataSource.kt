package com.example.sehatsehat.data.sources.remote

interface RemoteDataSource {
    suspend fun chatToChatbot(message:ChatBotDTO):ChatBotMessage
    suspend fun insertChatLog(chatlog:ChatLogDTO):ChatLogDRO
}