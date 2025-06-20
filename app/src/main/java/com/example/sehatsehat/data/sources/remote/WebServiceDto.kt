package com.example.sehatsehat.data.sources.remote

import com.example.sehatsehat.model.ChatLogEntity

data class ChatBotDTO(
    val message:String
)
data class ChatLogDTO(
    val content:String,
    val username:String,
    val chat_group:String
)

data class SyncChatGroupDTO(
    val group_id:String,
    val logs:List<ChatLogEntity>
)

data class LoginDTO(
    val username: String,
    val password: String
)

data class UserDTO(
    val username: String,
    val display_name: String,
    var password: String,
    var dob: String,
    var role: String,
    var pp_url: String
)

data class getUserProgramDTO(
    val username: String
)