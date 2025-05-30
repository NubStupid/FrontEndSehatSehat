package com.example.sehatsehat.data.sources.local

import com.example.sehatsehat.data.sources.DatabaseEntity
import java.util.Date

class RoomDataSource(
    private val db:AppDatabase
):LocalDataSource{


    override suspend fun getUnsynced(): DatabaseEntity {
        TODO("Not yet implemented")
    }

    override suspend fun sync(database: DatabaseEntity) {
        TODO("Not yet implemented")
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

    override suspend fun getFromChatbotChatLog(group_id: String): List<ChatLogEntity> {
        return db.chatLogDao().getChatFromBot(group_id)
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