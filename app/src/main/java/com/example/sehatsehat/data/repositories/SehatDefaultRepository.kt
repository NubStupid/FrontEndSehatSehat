package com.example.sehatsehat.data.repositories

import android.util.Log
import com.example.sehatsehat.model.ChatLogEntity
import com.example.sehatsehat.data.sources.local.LocalDataSource
import com.example.sehatsehat.data.sources.remote.ChatBotDTO
import com.example.sehatsehat.data.sources.remote.ChatLogDTO
import com.example.sehatsehat.data.sources.remote.LoginDRO
import com.example.sehatsehat.data.sources.remote.LoginDTO
import com.example.sehatsehat.data.sources.remote.ProgramDRO
import com.example.sehatsehat.data.sources.remote.ProgramListDRO
import com.example.sehatsehat.data.sources.remote.RegisterDRO
import com.example.sehatsehat.data.sources.remote.RemoteDataSource
import com.example.sehatsehat.data.sources.remote.UserDTO
import com.example.sehatsehat.model.ProgramEntity

class SehatDefaultRepository (
    private val localDataSource:LocalDataSource,
    private val remoteDataSource:RemoteDataSource
):SehatRepository{

    override suspend fun chatGroupSync(group_id: String) {
        val clientChatGroup = localDataSource.getFromGroupChatLog(group_id)
        Log.d("Test_Sync_clientCG",clientChatGroup.size.toString())
        val serverChatGroup = remoteDataSource.syncChatGroup(group_id,clientChatGroup)
        Log.d("Test_Sync_serverCG",serverChatGroup.chats.size.toString())
        localDataSource.syncChatGroup(group_id,serverChatGroup.chats)
    }

    override suspend fun getAllChatLog(): List<ChatLogEntity> {
        return localDataSource.getAllChatLog()
    }

    override suspend fun deleteChatLog(id: String) {
        val chatlog = localDataSource.deleteChatLog(id)

    }

    override suspend fun getFromChatbotChatLog(username: String): List<ChatLogEntity> {
        return localDataSource.getFromChatbotChatLog(username)
    }

    override suspend fun getFromGroupChatLog(group_id: String): List<ChatLogEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun updateChatLog(id: String, content: String) {
        TODO("Not yet implemented")
    }

    override suspend fun insertChatLog(
        content: String,
        username: String,
        chat_group: String
    ){
        localDataSource.insertChatLog(content, username, chat_group)
        remoteDataSource.insertChatLog(ChatLogDTO(content, username, chat_group))
    }


    override suspend fun chatToChatbot(content: String, username: String, chat_group: String) {
        insertChatLog(content,username, chat_group)
        val message = remoteDataSource.chatToChatbot(ChatBotDTO(content))
        insertChatLog(message.response,"Chatbot",chat_group)
    }

    // ==== (2) Auth / User ====
    override suspend fun registerUser(user: UserDTO): RegisterDRO {
        return remoteDataSource.registerUser(user)
    }

    override suspend fun loginUser(credentials: LoginDTO): LoginDRO {
        return remoteDataSource.loginUser(credentials)
    }

    // ==== (3) Program CRUD ====
    override suspend fun getAllPrograms(): List<ProgramEntity> {
        // Cek local pertama
        val localList = localDataSource.getAllPrograms()
        return if (localList.isNotEmpty()) {
            localList
        } else {
            // Jika kosong, fetch dari server
            val dro: ProgramListDRO = remoteDataSource.getAllPrograms()
            val dtoList: List<ProgramEntity> = dro.programs
            // Simpan semua ke local
            dtoList.forEach { localDataSource.insertProgram(it) }
            dtoList
        }
    }

    override suspend fun getProgramById(id: String): ProgramEntity? {
        val localProg = localDataSource.getProgramById(id)
        return if (localProg != null) {
            localProg
        } else {
            val dro: ProgramDRO = remoteDataSource.getProgramById(id)
            val progDTO: ProgramEntity = dro.program ?: return null
            localDataSource.insertProgram(progDTO)
            progDTO
        }
    }

    override suspend fun insertProgram(program: ProgramEntity) {
        // Kirim ke server
        val dro: ProgramDRO = remoteDataSource.insertProgram(program)
        val returned: ProgramEntity = dro.program ?: return
        // Simpan versi server (agar ID atau field lain sesuai backend)
        localDataSource.insertProgram(returned)
    }

    override suspend fun updateProgram(program: ProgramEntity) {
        // Kirim update ke server
        val dro: ProgramDRO = remoteDataSource.updateProgram(program.id, program)
        val returned: ProgramEntity = dro.program ?: return
        // Update di local
        localDataSource.updateProgram(returned)
    }

    override suspend fun deleteProgram(program: ProgramEntity) {
        // Hapus di server
        remoteDataSource.deleteProgram(program.id)
        // Hapus di local
        localDataSource.deleteProgram(program)
    }
}