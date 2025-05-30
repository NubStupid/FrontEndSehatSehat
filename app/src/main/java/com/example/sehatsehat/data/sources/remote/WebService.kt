package com.example.sehatsehat.data.sources.remote

import retrofit2.http.Body
import retrofit2.http.POST

interface WebService {
    @POST("/api/v1/chatbot")
    suspend fun chatToChatBot(@Body body: ChatBotDTO):ChatBotMessage

    @POST("/api/v1/chat")
    suspend fun addChatLog(@Body body:ChatLogDTO):ChatLogDRO


}