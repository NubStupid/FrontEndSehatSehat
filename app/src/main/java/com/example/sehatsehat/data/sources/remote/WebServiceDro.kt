package com.example.sehatsehat.data.sources.remote

import com.example.sehatsehat.model.ChatLogEntity
import com.example.sehatsehat.model.ProgramEntity
import com.example.sehatsehat.model.UserEntity

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

data class ProgramDRO(
    val status: Int,
    val message: String,
    val program: ProgramEntity? = null
)

/**
 * DRO untuk daftar Program. Misalnya respons GET /api/v1/programs.
 *
 * Contoh JSON respons:
 * {
 *   "status": 200,
 *   "programs": [
 *     { "id": "P001", "title": "Program A", "description": "Deskripsi A", "createdAt": 1686000000000 },
 *     { "id": "P002", "title": "Program B", "description": "Deskripsi B", "createdAt": 1686100000000 }
 *   ]
 * }
 */
data class ProgramListDRO(
    val status: Int,
    val programs: List<ProgramEntity>
)

data class UserListDRO(
    val status: Int,
    val users: List<UserEntity>
)
