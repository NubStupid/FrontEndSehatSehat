package com.example.sehatsehat.data.sources.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ChatLogDao {
    @Insert
    suspend fun insert(chatLog: ChatLogEntity):Unit
    @Update
    suspend fun update(chatLog: ChatLogEntity):Unit
    @Delete
    suspend fun delete(chatLog: ChatLogEntity):Unit
    @Query("SELECT * from chat_log where chat_group_id =:group_id")
    suspend fun getChatFromGroup(group_id:String):List<ChatLogEntity>
    @Query("SELECT * from chat_log where chat_group_id =:username+'_Chatbot'")
    suspend fun getChatFromBot(username:String):List<ChatLogEntity>

}