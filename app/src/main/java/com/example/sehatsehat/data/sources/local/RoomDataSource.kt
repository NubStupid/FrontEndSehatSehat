package com.example.sehatsehat.data.sources.local

import android.util.Log
import com.example.sehatsehat.model.ChatLogEntity
import java.util.Date

class RoomDataSource(
    private val db:AppDatabase
):LocalDataSource{


    override suspend fun syncChatGroup(group_id: String, logs: List<ChatLogEntity>) {
        db.chatLogDao().deleteGroupChat(group_id)
        for (log in logs){
            db.chatLogDao().insert(log)
        }
    }


    override suspend fun getAllChatLog(): List<ChatLogEntity> {
        return db.chatLogDao().getAll()
    }

    override suspend fun deleteChatLog(id: String): ChatLogEntity {
        val chatlog = db.chatLogDao().getChatFromId(id)
        if(chatlog != null){
            chatlog.deletedAt = Date().time
            db.chatLogDao().update(chatlog)
            return chatlog
        }else{
            throw NoSuchElementException("Chatlog with id: $id not found")
        }
    }

    override suspend fun getFromChatbotChatLog(username: String): List<ChatLogEntity> {
        Log.d("Room_check",username)
        return db.chatLogDao().getChatFromBot(username)
    }

    override suspend fun getFromGroupChatLog(group_id: String): List<ChatLogEntity> {
        return db.chatLogDao().getChatFromGroup(group_id)
    }

    override suspend fun updateChatLog(id: String, content: String): ChatLogEntity {
        val chatlog = db.chatLogDao().getChatFromId(id)
        if(chatlog != null){
            db.chatLogDao().update(chatlog.copy(content = content))
            return chatlog
        }else{
            throw NoSuchElementException("Chatlog with id: $id not found")
        }
    }

    override suspend fun insertChatLog(
        content: String,
        username: String,
        chat_group: String
    ): ChatLogEntity {
        var count = db.chatLogDao().getSize()
        val id = "CL"+(count+1).toString().padStart(5,'0')
        val chatlog = ChatLogEntity(id,chat_group,username,content)
        db.chatLogDao().insert(chatlog)
        return chatlog
    }
}