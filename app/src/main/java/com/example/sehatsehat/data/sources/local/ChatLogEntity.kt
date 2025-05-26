package com.example.sehatsehat.data.sources.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat_log")
data class ChatLogEntity(
    @PrimaryKey(false)
    val id:String,
    val chat_group_id:String,
    val username:String,
    val content:String
)