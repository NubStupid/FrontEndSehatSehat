package com.example.sehatsehat.data.sources.local

import com.example.sehatsehat.model.ChatLogEntity

interface LocalDataSource {
//    suspend fun getAll():List<Todo>
//    suspend fun getById(id:String): Todo?
//    suspend fun insert(content:String): Todo
//    suspend fun update(id:String, content:String, completed:Boolean): Todo
//    suspend fun delete(id:String): Todo

    suspend fun getAllChatLog():List<ChatLogEntity>
    suspend fun getFromGroupChatLog(group_id:String): List<ChatLogEntity>
    suspend fun getFromChatbotChatLog(username:String): List<ChatLogEntity>
    suspend fun insertChatLog(content:String, username:String,chat_group:String): ChatLogEntity
    suspend fun updateChatLog(id:String, content:String): ChatLogEntity
    suspend fun deleteChatLog(id:String): ChatLogEntity

    suspend fun syncChatGroup(group_id:String,logs:List<ChatLogEntity>)

}