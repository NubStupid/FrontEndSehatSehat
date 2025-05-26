package com.example.sehatsehat.data.sources.remote

import com.squareup.moshi.JsonClass
import java.util.Date

@JsonClass(generateAdapter = true)
data class ChatLogJson(
    val id:String,
    val chat_group_id:String,
    val username:String,
    val content:String,
    val createdAt: Long = Date().time,
    var updatedAt: Long = Date().time,
    var deletedAt: Long?=null
)
