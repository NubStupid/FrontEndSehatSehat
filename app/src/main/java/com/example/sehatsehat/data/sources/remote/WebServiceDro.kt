package com.example.sehatsehat.data.sources.remote

import com.example.sehatsehat.model.ChatLogEntity

data class ChatBotMessage(
    val response:String
)
data class ChatLogDRO(
    val status:Int,
    val message:String,
)
data class ChatLogFromGroupDRO(
    val status:Int,
    val chats:List<ChatLogEntity>
)

data class RegisterDRO(
    val message: String,
    val user: UserDRO? = null
)

data class LoginDRO(
    val message: String,
    val user: UserDRO? = null
)

data class UserDRO(
    val username: String,
    val display_name: String,
    val password: String,
    val dob: String,
    val role: String,
    val pp_url: String,
    val createdAt: String,
    val updatedAt: String
)

data class RegisterResponse(
    val message: String,
    val user: UserDRO? = null
)
