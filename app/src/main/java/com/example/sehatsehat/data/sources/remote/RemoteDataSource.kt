package com.example.sehatsehat.data.sources.remote

import com.example.sehatsehat.model.ChatLogEntity

interface RemoteDataSource {
    suspend fun chatToChatbot(message:ChatBotDTO):ChatBotMessage
    suspend fun insertChatLog(chatlog:ChatLogDTO):ChatLogDRO
    suspend fun getFromGroupChatLog(group_id:String):ChatLogFromGroupDRO
    suspend fun syncChatGroup(group_id:String, logs:List<ChatLogEntity>):ChatLogFromGroupDRO

}