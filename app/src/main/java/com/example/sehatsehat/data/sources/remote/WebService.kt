package com.example.sehatsehat.data.sources.remote

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface WebService {
    @POST("/api/v1/chatbot")
    suspend fun chatToChatBot(@Body body: ChatBotDTO):ChatBotMessage

    @POST("/api/v1/chat")
    suspend fun addChatLog(@Body body:ChatLogDTO):ChatLogDRO

    @GET("/api/v1/chat/{chat_group_id}")
    suspend fun getChatfromGroup(@Path("chat_group_id") chat_group_id:String):ChatLogFromGroupDRO

    @POST("/api/v1/chat/sync")
    suspend fun syncChatGroup(@Body body:SyncChatGroupDTO):ChatLogFromGroupDRO
}