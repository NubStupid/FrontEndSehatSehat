package com.example.sehatsehat.data.repositories

import android.util.Log
import com.example.sehatsehat.model.ChatLogEntity
import com.example.sehatsehat.data.sources.local.LocalDataSource
import com.example.sehatsehat.data.sources.remote.ChatBotDTO
import com.example.sehatsehat.data.sources.remote.ChatLogDTO
import com.example.sehatsehat.data.sources.remote.RemoteDataSource

class SehatDefaultRepository (
    private val localDataSource:LocalDataSource,
    private val remoteDataSource:RemoteDataSource
):SehatRepository{

    override suspend fun chatGroupSync(group_id: String) {
        val clientChatGroup = localDataSource.getFromGroupChatLog(group_id)
        Log.d("Test_Sync_clientCG",clientChatGroup.size.toString())
        val serverChatGroup = remoteDataSource.syncChatGroup(group_id,clientChatGroup)
        Log.d("Test_Sync_serverCG",serverChatGroup.chats.size.toString())
        localDataSource.syncChatGroup(group_id,serverChatGroup.chats)
    }

    override suspend fun getAllChatLog(): List<ChatLogEntity> {
        return localDataSource.getAllChatLog()
    }

    override suspend fun deleteChatLog(id: String) {
        val chatlog = localDataSource.deleteChatLog(id)

    }

    override suspend fun getFromChatbotChatLog(username: String): List<ChatLogEntity> {
        return localDataSource.getFromChatbotChatLog(username)
    }

    override suspend fun getFromGroupChatLog(group_id: String): List<ChatLogEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun updateChatLog(id: String, content: String) {
        TODO("Not yet implemented")
    }

    override suspend fun insertChatLog(
        content: String,
        username: String,
        chat_group: String
    ){
        localDataSource.insertChatLog(content, username, chat_group)
        remoteDataSource.insertChatLog(ChatLogDTO(content, username, chat_group))
    }


    override suspend fun chatToChatbot(content: String, username: String, chat_group: String) {
        insertChatLog(content,username, chat_group)
        val message = remoteDataSource.chatToChatbot(ChatBotDTO(content))
        insertChatLog(message.response,"Chatbot",chat_group)
    }
}