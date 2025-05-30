package com.example.sehatsehat.data.sources.local

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
    )