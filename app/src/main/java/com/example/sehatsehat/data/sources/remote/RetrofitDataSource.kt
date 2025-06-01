package com.example.sehatsehat.data.sources.remote

import com.example.sehatsehat.model.ChatLogEntity

class RetrofitDataSource(
    private val retrofitService:WebService
):RemoteDataSource{
    override suspend fun chatToChatbot(message: ChatBotDTO): ChatBotMessage {
        return retrofitService.chatToChatBot(message)
    }

    override suspend fun insertChatLog(chatlog: ChatLogDTO): ChatLogDRO {
        return retrofitService.addChatLog(chatlog)
    }

    override suspend fun getFromGroupChatLog(group_id: String): ChatLogFromGroupDRO {
        return  retrofitService.getChatfromGroup(group_id)
    }

    override suspend fun syncChatGroup(group_id: String, logs: List<ChatLogEntity>):ChatLogFromGroupDRO {
        return retrofitService.syncChatGroup(SyncChatGroupDTO(group_id,logs))
    }
}