package com.example.sehatsehat.data.repositories

import com.example.sehatsehat.model.ChatLogEntity

interface SehatRepository{
    suspend fun chatGroupSync(group_id: String)

    suspend fun getAllChatLog():List<ChatLogEntity>
    suspend fun getFromGroupChatLog(group_id:String): List<ChatLogEntity>
    suspend fun getFromChatbotChatLog(username:String): List<ChatLogEntity>
    suspend fun insertChatLog(content:String, username:String,chat_group:String)
    suspend fun updateChatLog(id:String, content:String)
    suspend fun deleteChatLog(id:String)

    suspend fun chatToChatbot(content:String,username:String,chat_group: String)

}