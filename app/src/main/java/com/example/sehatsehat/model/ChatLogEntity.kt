package com.example.sehatsehat.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass
import java.util.Date

    @JsonClass(generateAdapter = true)
    @Entity(tableName = "chat_log")
    data class ChatLogEntity(
        @PrimaryKey(false)
        val id:String,
        val chat_group_id:String,
        val username:String,
        val content:String,
        val createdAt: Long = Date().time,
        var updatedAt: Long = Date().time,
        var deletedAt: Long?=null
    ){
        init{
            if(content.isNullOrEmpty()){
                throw IllegalArgumentException("Content must not be empty!")
            }
            if(username.isNullOrEmpty()){
                throw IllegalArgumentException("Username must not be empty!")
            }
            if(chat_group_id.isNullOrEmpty()){
                throw IllegalArgumentException("Chat group id must not be empty!")
            }
        }
    }