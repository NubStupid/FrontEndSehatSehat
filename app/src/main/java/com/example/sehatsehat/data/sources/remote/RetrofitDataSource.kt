package com.example.sehatsehat.data.sources.remote

class RetrofitDataSource(
    private val retrofitService:WebService
):RemoteDataSource{
    override suspend fun chatToChatbot(message: ChatBotDTO): ChatBotMessage {
        return retrofitService.chatToChatBot(message)
    }

    override suspend fun insertChatLog(chatlog: ChatLogDTO): ChatLogDRO {
        return retrofitService.addChatLog(chatlog)
    }
}