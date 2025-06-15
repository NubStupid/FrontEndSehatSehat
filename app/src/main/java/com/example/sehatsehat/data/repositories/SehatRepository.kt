package com.example.sehatsehat.data.repositories

import com.example.sehatsehat.data.sources.remote.LoginDRO
import com.example.sehatsehat.data.sources.remote.LoginDTO
import com.example.sehatsehat.data.sources.remote.RegisterDRO
import com.example.sehatsehat.data.sources.remote.UserDTO
import com.example.sehatsehat.model.ChatLogEntity
import com.example.sehatsehat.model.ProgramEntity
import com.example.sehatsehat.model.UserEntity

interface SehatRepository{
    suspend fun chatGroupSync(group_id: String)

    suspend fun getAllChatLog():List<ChatLogEntity>
    suspend fun getFromGroupChatLog(group_id:String): List<ChatLogEntity>
    suspend fun getFromChatbotChatLog(username:String): List<ChatLogEntity>
    suspend fun insertChatLog(content:String, username:String,chat_group:String)
    suspend fun updateChatLog(id:String, content:String)
    suspend fun deleteChatLog(id:String)

    suspend fun chatToChatbot(content:String,username:String,chat_group: String)

    // ==== Auth / User ====
    suspend fun registerUser(user: UserDTO): RegisterDRO
    suspend fun loginUser(credentials: LoginDTO): LoginDRO

    // ==== Program CRUD ====
    suspend fun getAllPrograms(): List<ProgramEntity>
    suspend fun getProgramById(id: String): ProgramEntity?
    suspend fun insertProgram(program: ProgramEntity)
    suspend fun updateProgram(program: ProgramEntity)
    suspend fun deleteProgram(program: ProgramEntity)

//    user crud
    suspend fun getAllUsers(): List<UserEntity>
    suspend fun deleteUser(user: UserEntity)
}